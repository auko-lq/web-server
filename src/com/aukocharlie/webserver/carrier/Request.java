package com.aukocharlie.webserver.carrier;

import com.aukocharlie.webserver.enums.ExceptionLevelEnum;
import com.aukocharlie.webserver.exception.BaseException;
import com.aukocharlie.webserver.exception.InvalidQueryStringException;
import com.aukocharlie.webserver.logger.LoggerTemplate;
import com.aukocharlie.webserver.utils.URLUtils;
import lombok.Data;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

@Data
public class Request extends LoggerTemplate {

    public Request(InputStream inputStream, Socket socket) throws IOException {
        parse(inputStream, socket);
    }

    private boolean valid;
    private String method;
    private String requestURI;
    private boolean keepAlive;
    private int localPort;
    private int port;
    private String queryString;
    private Map<String, String> parameterMap = new HashMap<>();


    public void parse(InputStream inputStream, Socket socket) throws IOException {
        String message = byteToString(inputStream);
        if (!"".equals(message)) {
            String method = parseMethod(message);
            String requestURI = parseRequestURI(message);

            if (method == null || requestURI == null) {
                this.valid = false;
            } else {
                logger.debug("request : " + method + " " + requestURI);
                requestURI = removeQueryString(requestURI);
                setMethod(method);
                setRequestURI(requestURI);
                this.valid = true;
            }
        } else {
            this.valid = false;
        }
        parseSocket(socket);
    }

    /**
     * 将请求报文解析成字符串
     *
     * @param inputStream 字节流
     * @return 报文字符串
     */
    public String byteToString(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer message = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null && !"".equals(line)) {
            message.append(line);
        }
        return message.toString();
    }

    /**
     * 解析socket信息
     *
     * @param socket socket
     */
    public void parseSocket(Socket socket) {
        try {
            this.keepAlive = socket.getKeepAlive();
            this.localPort = socket.getLocalPort();
            this.port = socket.getPort();
        } catch (SocketException e) {
            throw new BaseException(ExceptionLevelEnum.WARN, "Error getting socket information", e);
        }
    }

    /**
     * 去除queryString
     *
     * @param requestURI URI
     * @return 去除后的URI
     */
    public String removeQueryString(String requestURI) {
        int index1 = requestURI.indexOf('?');
        if (index1 > -1) {
            this.queryString = requestURI.substring(index1 + 1);
            logger.debug("queryString : " + queryString);
            parseQueryString(queryString);
            requestURI = requestURI.substring(0, index1);
        }
        return requestURI;
    }

    /**
     * 解析query string
     *
     * @param queryString queryString
     */
    public void parseQueryString(String queryString) {
        try {
            for (String item : queryString.split("&")) {
                String[] parameter = item.split("=");
                if (parameter.length != 2) {
                    throw new InvalidQueryStringException("Invalid queru string : " + queryString);
                } else {
                    String fieldName = URLUtils.decodeURL(parameter[0]);
                    String value = URLUtils.decodeURL(parameter[1]);
                    parameterMap.put(fieldName, value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new BaseException(ExceptionLevelEnum.WARN, "Unsupported encoding url", e);
        }
    }

    /**
     * 解析请求方式
     *
     * @param message 请求报文
     * @return 请求方式
     */
    public String parseMethod(String message) {
        // 通过找到第一个空格的位置, 来确定请求方式
        int index = message.indexOf(' ');
        if (index != -1) {
            return message.substring(0, index);
        }
        return null;
    }

    /**
     * 从报文中解析出URI
     *
     * @param message 请求报文
     * @return 返回URI
     */
    public String parseRequestURI(String message) {
        int index1, index2;
        // 找到第一个 '/'
        index1 = message.indexOf('/');
        if (index1 != -1) {
            index2 = message.indexOf(' ', index1);
            if (index2 > index1) {
                return message.substring(index1, index2);
            }
        }
        return null;
    }
}
