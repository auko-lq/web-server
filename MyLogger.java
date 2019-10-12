package myServer;
import	java.io.InputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @packageName: myServer
 * @className: MyLogger
 * @Description: 日志类
 * @author: auko
 * @data 2019-10-09 13:59
 */
public class MyLogger {
    Logger logger;
    public MyLogger(Class cla){
        logger = Logger.getLogger(cla);
        InputStream in = getClass().getResourceAsStream("/myServer/conf/log4j.properties");
        PropertyConfigurator.configure(in);
        logger.setLevel(Level.TRACE);
    }
}
