package com.dynata.surveyhw.utils;

import com.querydsl.core.types.Order;
import jakarta.ws.rs.ext.ParamConverter;

import java.util.LinkedHashMap;
import java.util.Map;

public class SortParamConverter implements ParamConverter<Map<String, Order>> {

    @Override
    public Map<String, Order> fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        Map<String, Order> map = new LinkedHashMap<>();

        // Ez egyetlen "sort" param érték -> pl. "firstName,asc"
        String[] kv = value.split(",");
        if (kv.length == 2) {
            String field = kv[0].trim();
            String direction = kv[1].trim().toUpperCase();
            try {
                map.put(field, Order.valueOf(direction));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid sort direction: " + direction);
            }
        }

        return map;
    }

    @Override
    public String toString(Map<String, Order> value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        value.forEach((k, v) -> sb.append("sort=").append(k).append(",").append(v.name().toLowerCase()).append("&"));
        return sb.toString().replaceAll("&$", "");
    }
}
