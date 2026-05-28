package com.campus.marketplace.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {

    private List<T> records;
    private long total;
    private long size;
    private long current;
    private long pages;

    public static <T> PageResponse<T> of(List<T> records, long total, long size, long current) {
        PageResponse<T> response = new PageResponse<>();
        response.setRecords(records);
        response.setTotal(total);
        response.setSize(size);
        response.setCurrent(current);
        response.setPages((total + size - 1) / size);
        return response;
    }
}
