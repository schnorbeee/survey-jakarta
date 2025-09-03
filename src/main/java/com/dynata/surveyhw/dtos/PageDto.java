package com.dynata.surveyhw.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageDto<T> {

    @JsonProperty("content")
    public List<T> content;

    @JsonProperty("pageNumber")
    public int pageNumber;

    @JsonProperty("pageSize")
    public int pageSize;

    @JsonProperty("totalElements")
    public long totalElements;

    @JsonProperty("numberOfElements")
    public int numberOfElements;

    public PageDto(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.numberOfElements = content.size();
    }
}