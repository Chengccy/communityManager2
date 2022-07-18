package com.nk.entity;

import lombok.Data;

import java.util.List;

@Data
public class JsonResult<T> {
    private Integer code;
    private String msg;
    private Long count;
    private List<T> data;
}
