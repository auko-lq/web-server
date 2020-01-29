package com.aukocharlie.webserver.exception;

import com.aukocharlie.webserver.enums.ExceptionLevelEnum;

/**
 * @author: auko
 * @data 2020-01-27 18:24
 */
public class StatusCodeUnmanagedException extends BaseException {
    public StatusCodeUnmanagedException(){
        super(ExceptionLevelEnum.ERROR, "Existing unmanaged status code", new RuntimeException());
    }

    public StatusCodeUnmanagedException(String msg){
        super(ExceptionLevelEnum.ERROR, msg, new RuntimeException());
    }
}
