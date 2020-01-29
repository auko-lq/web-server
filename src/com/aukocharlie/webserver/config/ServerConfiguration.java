package com.aukocharlie.webserver.config;

import com.aukocharlie.webserver.enums.ExceptionLevelEnum;
import com.aukocharlie.webserver.exception.BaseException;
import com.aukocharlie.webserver.logger.LoggerTemplate;
import com.aukocharlie.webserver.utils.XmlParser;
import lombok.Data;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.Iterator;

/**
 * @packageName: com.aukocharlie.webserver
 * @className: Configuration
 * @Description:
 * @author: auko
 * @data 2020-01-22 15:45
 */
@Data
public class ServerConfiguration extends LoggerTemplate {

    private volatile static ServerConfiguration config;

    public static ServerConfiguration getInstance(String configurationPath) {
        if (null == config) {
            synchronized (ServerConfiguration.class) {
                if (null == config) {
                    config = new ServerConfiguration(configurationPath);
                }
            }
        }
        return config;
    }

    public static ServerConfiguration getInstance(){
        if (null == config) {
            synchronized (ServerConfiguration.class) {
                if (null == config) {
                    config = new ServerConfiguration();
                }
            }
        }
        return config;
    }

    /**
     * the root path of webServer
     */
    private String rootPath = System.getProperty("user.dir");

    /**
     * apps path
     * The default path is the webapps folder in the root directory
     */
    private String contextPath = rootPath + "/webapps";

    /**
     * Default port is 8889
     */
    private int port = 8889;

    /**
     * Using default configuration
     */
    public ServerConfiguration(){}

    public ServerConfiguration(String configurationPath) {
        readConfiguration(configurationPath);
    }

    /**
     * read server configuration
     *
     * @param configurationPath the relative path of configuration file
     */
    private void readConfiguration(String configurationPath) {
        try {
            XmlParser xmlParser = new XmlParser(configurationPath);
            Iterator contextIterator = xmlParser.getIteratorByXPath("/src/com/aukocharlie/webserver/context");
            // 暂时只支持单应用, 只取第一个
            if (contextIterator.hasNext()) {
                Element contextElement = (Element) contextIterator.next();
                Iterator pathIterator = contextElement.elementIterator("path");
                Iterator portIterator = contextElement.elementIterator("port");

                if (pathIterator.hasNext()) contextPath = ((Element) pathIterator.next()).getText();
                if (portIterator.hasNext()) port = Integer.parseInt(((Element) portIterator.next()).getText());
            }
        } catch (DocumentException e) {
            throw new BaseException(ExceptionLevelEnum.ERROR,
                    "Error reading configuration file, default configuration will be used", e);
        } catch (NumberFormatException e) {
            throw new BaseException(ExceptionLevelEnum.ERROR,
                    "port should be digital, default port(8889) will be used", e);
        }
    }
}
