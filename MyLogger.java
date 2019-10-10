package myApache;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @packageName: myApache
 * @className: MyLogger
 * @Description: 日志类
 * @author: auko
 * @data 2019-10-09 13:59
 */
public class MyLogger {
    Logger logger;
    public MyLogger(Class cla){
        logger = Logger.getLogger(cla);
        PropertyConfigurator.configure("src/myApache/conf/log4j.properties");
        logger.setLevel(Level.TRACE);
    }
}
