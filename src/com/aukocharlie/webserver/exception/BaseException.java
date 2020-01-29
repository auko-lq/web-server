package com.aukocharlie.webserver.exception;

import com.aukocharlie.webserver.enums.ExceptionLevelEnum;
import lombok.Data;


/**
 * 包装所有异常, 用于统一打日志
 */
@Data
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ExceptionLevelEnum level;

    private String message;

    /**
     * 通过自定义的等级枚举来构造
     *
     * @param level     异常等级
     * @param message   异常信息
     * @param exception 异常本身
     */
    public BaseException(ExceptionLevelEnum level, String message, Throwable exception) {
        super(message, exception);
        this.level = level;
        this.message = message;
    }


    /**
     * 通过自定义等级对应的四个数字来构造
     *
     * @param level     等级数字
     * @param message   异常信息
     * @param exception 异常本身
     */
    public BaseException(int level, String message, Throwable exception) {
        super(message, exception);
        ExceptionLevelEnum temp = ExceptionLevelEnum.valueOf(level);
        if (temp == null) {
            throw new Error("Exception level unmanaged !!");
        } else {
            this.level = temp;
            this.message = message;
        }
    }

}
