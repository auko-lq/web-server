package com.aukocharlie.webserver.enums;

import java.util.stream.Stream;

/**
 * 区分异常的等级, 对应不同日志级别
 *
 * @author auko
 * @date 2020-01-27 1:06
 */
public enum ExceptionLevelEnum {
    /**
     * 只采用四个等级, debug为最低, error为最高
     */
    DEBUG(0, "DEBUG"),
    INFO(1, "INFO"),
    WARN(2, "WARN"),
    ERROR(3, "ERROR");

    private int level;
    private String description;

    ExceptionLevelEnum(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public static ExceptionLevelEnum valueOf(int value) {
        return Stream.of(values()).filter(level -> level.getLevel() == value).findAny().orElse(null);
    }


    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }
}
