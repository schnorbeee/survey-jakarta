-- csv_upload.lua

-- Fájlok és endpoint mapping
files = {
  {path = "./src/test/resources/testfiles/Members.csv", endpoint = "/api/members"},
  {path = "./src/test/resources/testfiles/Statuses.csv", endpoint = "/api/statuses"},
  {path = "./src/test/resources/testfiles/Surveys.csv", endpoint = "/api/surveys"},
  {path = "./src/test/resources/testfiles/Participations.csv", endpoint = "/api/participations"}
}

-- Statisztikák és hibák gyűjtése
stats = {}
errors = {}
for _, f in ipairs(files) do
  stats[f.endpoint] = {total = 0, success = 0}
  errors[f.endpoint] = {}         -- lista mintákhoz
  errors[f.endpoint].counts = {}  -- gyakoriság számláló
end

-- Multipart body builder
function multipart_body(file_path)
  local boundary = "------------------------" .. tostring(math.random(1e10,1e11-1))
  local filename = file_path:match("^.+/(.+)$") or "upload.csv"

  local f = io.open(file_path, "r")
  local content = f:read("*all")
  f:close()

  local body = "--" .. boundary .. "\r\n" ..
               'Content-Disposition: form-data; name="file"; filename="' .. filename .. '"\r\n' ..
               "Content-Type: text/csv\r\n\r\n" ..
               content .. "\r\n" ..
               "--" .. boundary .. "--\r\n"

  local headers = {
    ["Content-Type"] = "multipart/form-data; boundary=" .. boundary,
    ["Content-Length"] = #body,
    ["X-Endpoint"] = "" -- ide kerül majd dinamikusan
  }

  return body, headers
end

-- Kérés összeállítása
function request()
  local f = files[math.random(#files)]
  local body, headers = multipart_body(f.path)
  headers["X-Endpoint"] = f.endpoint

  stats[f.endpoint].total = stats[f.endpoint].total + 1
  return wrk.format("POST", f.endpoint, headers, body)
end

-- Válasz feldolgozása
function response(status, headers, body)
  local endpoint = headers["X-Endpoint"]
  if not endpoint or not stats[endpoint] then return end

  if status >= 200 and status < 300 then
    stats[endpoint].success = stats[endpoint].success + 1
  else
    local msg = body:match('"message"%s*:%s*"([^"]+)"') or "n/a"
    local detail = body:match('"detail"%s*:%s*"([^"]+)"') or "n/a"
    local key = msg .. " | " .. detail

    -- előfordulásszám növelése
    if errors[endpoint].counts[key] then
      errors[endpoint].counts[key] = errors[endpoint].counts[key] + 1
    else
      errors[endpoint].counts[key] = 1
    end

    -- mintákhoz pár konkrét példa
    if #errors[endpoint] < 5 then
      table.insert(errors[endpoint], {status=status, message=msg, detail=detail})
    end
  end
end

-- Teszt végi összegzés
function done(summary, latency, requests)
  io.stderr:write("\n--- CSV Upload Summary ---\n")
  for endpoint, s in pairs(stats) do
    local ratio = 0
    if s.total > 0 then
      ratio = (s.success / s.total) * 100
    end
    io.stderr:write(string.format(
      "%s -> összes: %d, sikeres: %d (%.1f%%), hibák: %d\n",
      endpoint, s.total, s.success, ratio, s.total - s.success
    ))

    -- Minták
    for _, err in ipairs(errors[endpoint]) do
      io.stderr:write(string.format(
        "   [HTTP %d] message='%s', detail='%s'\n",
        err.status, err.message, err.detail
      ))
    end

    -- Top lista message+detail szerint
    if next(errors[endpoint].counts) ~= nil then
      io.stderr:write("   --- Top hibák ---\n")
      for key, count in pairs(errors[endpoint].counts) do
        io.stderr:write(string.format("   (%d) %s\n", count, key))
      end
    end
  end
  io.stderr:write("--------------------------\n")
end
