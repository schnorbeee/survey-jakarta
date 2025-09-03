package com.dynata.surveyhw.dtos;

import com.querydsl.core.types.Order;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageRequest {

    @QueryParam("page")
    @DefaultValue("1")
    private int pageNumber;

    @QueryParam("size")
    @DefaultValue("20")
    private int pageSize;

    @QueryParam("sort")
    private Map<String, Order> sort;

    public long getOffset() {
        int dbPage = Math.max(0, pageNumber - 1);
        return (long) dbPage * pageSize;
    }
}