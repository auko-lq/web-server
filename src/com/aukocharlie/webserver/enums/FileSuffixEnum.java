package com.aukocharlie.webserver.enums;

import java.util.stream.Stream;

/**
 * 文件后缀名枚举
 */
public enum FileSuffixEnum {
    HTML("html", "text/html"),
    ICO("ico", "image/vnd.microsoft.icon"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JS("js", "text/javascript"),
    CSS("css", "text/css");

    private String suffix;
    private String contentType;

    FileSuffixEnum(String suffix, String contentType) {
        this.suffix = suffix;
        this.contentType = contentType;
    }

    public static FileSuffixEnum getEnum(String value) {
        return Stream.of(values()).filter(suffix -> suffix.getSuffix().equals(value)).findAny().orElse(null);
    }

    public String getSuffix() {
        return suffix;
    }

    public String getContentType() {
        return contentType;
    }
}
