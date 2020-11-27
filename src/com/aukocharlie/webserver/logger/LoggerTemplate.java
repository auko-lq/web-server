package com.aukocharlie.webserver.logger;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * use Log4j by extends LoggerTemplate
 *
 * @author auko
 * @date 2020-01-22 16:03
 */
public class LoggerTemplate {

    protected Log logger;

    public LoggerTemplate() {
        Class clazz = this.getClass();
        this.logger = LogFactory.getLog((Class)clazz.getGenericSuperclass());
    }
}
