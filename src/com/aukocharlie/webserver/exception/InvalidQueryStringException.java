package com.aukocharlie.webserver.exception;

import com.aukocharlie.webserver.enums.ExceptionLevelEnum;

/**
 * @author auko
 * @date 2020-01-28 16:42
 */
public class InvalidQueryStringException extends BaseException {
    public InvalidQueryStringException(){
        super(ExceptionLevelEnum.WARN, "Invalid query string", new RuntimeException());
    }

    public InvalidQueryStringException(String msg){
        super(ExceptionLevelEnum.WARN, msg, new RuntimeException());
    }
}
