package com.aukocharlie.webserver.exception;

import com.aukocharlie.webserver.enums.ExceptionLevelEnum;

/**
 * @author: auko
 * @data 2020-01-27 23:32
 */
public class FileSuffixUnmanagedException extends BaseException {
    public FileSuffixUnmanagedException(){
        super(ExceptionLevelEnum.ERROR, "Existing unmanaged file suffix", new RuntimeException());
    }

    public FileSuffixUnmanagedException(String msg){
        super(ExceptionLevelEnum.ERROR, msg, new RuntimeException());
    }
}
