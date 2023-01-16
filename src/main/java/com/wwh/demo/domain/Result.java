package com.wwh.demo.domain;

import lombok.Data;

@Data
public class Result {
    private int code;
    private String message;

    public Result(int code, String message) {
        this.message=message;
        this.code=code;
    }
}
