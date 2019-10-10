package myApache;


import org.apache.log4j.Logger;

import java.util.logging.Level;

import java.io.IOException;
import java.io.InputStream;

/**
 * @packageName: myApache
 * @className: Request
 * @Description:
 * @author: auko
 * @data 2019-10-08 14:14
 */
public class Request {
    public static void sop(Object obj) {
        System.out.println(obj);
    }

    private static final int BUFFER_SIZE = 1024;

    public Logger logger = new MyLogger(Request.class).logger;

    /**
     * @Param: [ins]
     * @Author: auko on 2019-10-09 22:40
     * @Description:  构造方法, 运行parse
     */
    public Request(InputStream ins){
        parse(ins);
    }

    // 请求方式
    private String type;

    // 请求uri
    private String uri;

    /**
     * @Param: []
     * @Return: java.lang.String
     * @Author: auko on 2019-10-09 12:16
     * @Description: 获取请求类型
     */
    public String getType() {
        return type;
    }

    /**
     * @Param: [type]
     * @Return: void
     * @Author: auko on 2019-10-09 12:17
     * @Description: 设置请求类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @Param: []
     * @Return: java.lang.String
     * @Author: auko on 2019-10-09 12:17
     * @Description: 获取请求uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @Param: [uri]
     * @Return: void
     * @Author: auko on 2019-10-09 12:17
     * @Description: 设置请求uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @Param: [ins]
     * @Return: void
     * @Author: auko on 2019-10-09 12:18
     * @Description: 解析请求
     */
    public void parse(InputStream ins) {
        String requestMsg = readMsg(ins);
        String type = parseType(requestMsg);
        String uri = parseUri(requestMsg);

        if (type != null || uri != null) {
            logger.trace("request : " + type + " " + uri);
            setType(type);
            setUri(uri);
        }else{
            logger.warn("request info exception : type or uri exception(null)");
        }
    }

    /**
     * @Param: [ins]
     * @Return: java.lang.String
     * @Author: auko on 2019-10-09 12:19
     * @Description: 通过字节流读取请求信息
     */
    public String readMsg(InputStream ins) {
        StringBuffer requestMsg = new StringBuffer();
        int readLen = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            readLen = ins.read(buffer);
        } catch (IOException e) {
            readLen = -1;
            throw new RuntimeException(e);
        }
        for (int i = 0; i < readLen; i++) {
            requestMsg.append((char) buffer[i]);
        }
        return requestMsg.toString();
    }

    /*
     * @Param: [requestMsg]
     * @Return: java.lang.String
     * @Author: auko on 2019-10-09 12:40
     * @Description: 解析请求方式
     */
    public String parseType(String requestMsg) {
        // 通过找到第一个空格的位置, 才确定请求方式
        int index = requestMsg.indexOf(' ');
        if (index != -1) {
            return requestMsg.substring(0, index);
        }
        return null;
    }

    /**
     * @Param: [requestMsg]
     * @Return: java.lang.String
     * @Author: auko on 2019-10-09 13:32
     * @Description: 解析uri
     */
    public String parseUri(String requestMsg) {
        int index1, index2;
        // 找到第一个 '/'
        index1 = requestMsg.indexOf('/');
        if (index1 != -1) {
            index2 = requestMsg.indexOf(' ', index1);
            if (index2 > index1) {
                return requestMsg.substring(index1, index2);
            }
        }
        return null;
    }

}
