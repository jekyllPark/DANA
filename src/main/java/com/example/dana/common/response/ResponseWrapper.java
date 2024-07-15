package com.example.dana.common.response;

public record ResponseWrapper<T>(ResponseStatus status, String message, T data) {
    public static <T> ResponseWrapper<T> SUCCESS(String message, T data) {
        return new ResponseWrapper(ResponseStatus.SUCCESS, message, data);
    }

    public static <T> ResponseWrapper<T> FAILURE(String message, T data) {
        return new ResponseWrapper(ResponseStatus.FAILURE, message, data);
    }
}