package com.aukocharlie.webserver.enums;

import java.util.stream.Stream;

/**
 * http 状态码枚举
 */
public enum HttpStatusEnum {

    OK(200, "OK"),
    CREATED(201, "Created"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "File Not Found"),
    SERVER_ERROR(500, "Internal Server Error"),
    FOUND(302, "Found");

    private int status;
    private String description;

    HttpStatusEnum(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public static HttpStatusEnum valueOf(int value) {
        return Stream.of(values()).filter(status -> status.getStatus() == value).findAny().orElse(null);
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
