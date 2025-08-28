-- sync_requests_POST.lua
-- Szinkron POST/GET kérések, hibák message/detail loggal
-- POST elöl, GET után

local endpoints = {
    { method = "POST", path = "/api/statuses", file = "./src/test/resources/testfiles/Statuses.csv" },
    { method = "POST", path = "/api/members", file = "./src/test/resources/testfiles/Members.csv" },
    { method = "POST", path = "/api/surveys", file = "./src/test/resources/testfiles/Surveys.csv" },
    { method = "POST", path = "/api/participations", file = "./src/test/resources/testfiles/Participations.csv" }
--     { method = "GET", path = "/api/members/by-survey-and-completed?surveyId=1" },
--     { method = "GET", path = "/api/members/by-not-participated-survey-and-active?surveyId=1" },
--     { method = "GET", path = "/api/surveys/by-member-id-and-completed?memberId=1" },
--     { method = "GET", path = "/api/surveys/by-member-id-completion-points?memberId=1" },
--     { method = "GET", path = "/api/surveys/all-statistic" }
}

-- Előre beolvassuk a fájlokat egyszer, hogy ne legyen I/O probléma
local file_contents = {}
for i, e in ipairs(endpoints) do
    local f = io.open(e.file, "rb")
    if f then
        file_contents[i] = f:read("*all")
        f:close()
    else
        file_contents[i] = ""
    end
end

request = function()
    local i = math.random(1, #endpoints)
    local e = endpoints[i]
    local body = "--boundary\r\nContent-Disposition: form-data; name=\"file\"; filename=\""..e.file.."\"\r\nContent-Type: text/csv\r\n\r\n"..file_contents[i].."\r\n--boundary--\r\n"
    local headers = { ["Content-Type"] = "multipart/form-data; boundary=boundary" }
    return wrk.format(e.method, e.path, headers, body)
end

response = function(status, headers, body)
    if status >= 400 then
        local msg, detail = body:match('"message"%s*:%s*"([^"]*)".-"detail"%s*:%s*"([^"]*)"')
        print("HTTP "..status.." Error: message="..(msg or "N/A")..", detail="..(detail or "N/A"))
    end
end