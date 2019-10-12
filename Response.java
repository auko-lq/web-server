package myServer;

import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @packageName: myServer
 * @className: Response
 * @Description: 回复类
 * @author: auko
 * @data 2019-10-09 14:38
 */
public class Response {
    public Logger logger = new MyLogger(Response.class).logger;
    private OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }


    /**
     * @Param: [statusCode, contentType, connection]
     * @Return: void
     * @Author: auko on 2019-10-10 15:38
     * @Description: 发送应答报文, 不发送文件长度
     */
    public void sendHeader(int statusCode, String contentType, String connection) {
        sendHeader(statusCode, contentType, connection, 0,null,0);
    }


    /**
     * @Param: [statusCode, contentType, connection, contentLength, lastModified]
     * @Return: void
     * @Author: auko on 2019-10-10 22:33
     * @Description: 专为文件应答打造
     */
    public void sendHeader(int statusCode, String contentType, String connection, long contentLength, long lastModified) {
        sendHeader(statusCode, contentType, connection, contentLength,null, lastModified);
    }


    /**
     * @Param: [statusCode, contentType, connection, contentLength, location, lastModified]
     * @Return: void
     * @Author: auko on 2019-10-10 15:37
     * @Description: 发送应答报文
     */
    public void sendHeader(int statusCode, String contentType, String connection, long contentLength, String location, long lastModified) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
        Date nowTime = new Date();
        String dateStr = sdf.format(nowTime);

        String acceptRanges = "bytes";
        try {
            // 根据不同的状态码发送不同的文本
            sendStatusCode(statusCode);

            output.write(String.format("Accept-Ranges: %s\n", acceptRanges).getBytes());
            if (connection != null) {
                output.write(String.format("Connection: %s\n", connection).getBytes());
            }
            if (contentLength != 0) {
                output.write(String.format("Content-Length: %d\n", contentLength).getBytes());
            }
            output.write(String.format("Content-Type: %s\n", contentType).getBytes());
            output.write(String.format("Date: %s GMT\n", dateStr).getBytes());
            if (location != null) {
                output.write(String.format("Location: %s\n", location).getBytes());
            }
            if(lastModified != 0){
                String lastModifiedStr = sdf.format(lastModified);
                output.write(String.format("Last-Modified: %s\n", lastModifiedStr).getBytes());
            }
            output.write("\n".getBytes());
        } catch (IOException e) {
            logger.error("response error : " + e.getMessage());
        }
    }


    /**
     * @Param: [statusCode]
     * @Return: void
     * @Author: auko on 2019-10-10 19:34
     * @Description: 发送应答报文的状态码
     */
    public void sendStatusCode(int statusCode) {
        try {
            switch (statusCode) {
                case 200:
                    output.write(String.format("HTTP/1.1 %d OK\n", statusCode).getBytes());
                    break;
                case 404:
                    output.write(String.format("HTTP/1.1 %d File Not Found\n", statusCode).getBytes());
                    break;
                case 201:
                    output.write(String.format("HTTP/1.1 %d Created\n", statusCode).getBytes());
                    break;
                case 400:
                    output.write(String.format("HTTP/1.1 %d Bad Request\n", statusCode).getBytes());
                    break;
                case 500:
                    output.write(String.format("HTTP/1.1 %d Internal Server Error\n", statusCode).getBytes());
                    break;
                case 302:
                    output.write(String.format("HTTP/1.1 %d Found\n", statusCode).getBytes());
                    break;
                default:
                    // 不在上面几个里面的话就返回500
                    output.write(String.format("HTTP/1.1 %d Internal Server Error\n", statusCode).getBytes());
                    logger.warn("can't handle the statusCode : "+statusCode);
                    break;
            }
        } catch (IOException e) {
            logger.error("response error");
        }
    }


    /**
     * @Param: [statusCode, contentType, text]
     * @Return: void
     * @Author: auko on 2019-10-10 23:24
     * @Description: 回复文本, 自定义状态码
     */
    public void send(int statusCode, String contentType, String text) {
        try {
            sendHeader(statusCode, contentType, "keep-alive");
            output.write(text.getBytes());
        } catch (IOException e) {
            logger.error("response error");
        }
    }

    /**
     * @Param: [file, contentType]
     * @Return: void
     * @Author: auko on 2019-10-10 14:33
     * @Description: 发送文件
     */
    public void sendFile(File file, String contentType) {
        try (
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
        ) {
            sendHeader(200, contentType, "keep-alive", file.length(), file.lastModified());
            byte[] b = new byte[1024];
            int len; //记录每次读取数据并存入数组中后的返回值，代表读取到的字节数，最大值为b.length；当输入文件被读取完后返回-1
            while ((len = bis.read(b)) != -1) {
                output.write(b, 0, len);
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @Param: [pic, contentType]
     * @Return: void
     * @Author: auko on 2019-10-10 22:06
     * @Description: 发送图片
     */
    public void sendPic(File pic, String contentType) {
        sendFile(pic, contentType);
    }

    /**
     * @Param: []
     * @Return: void
     * @Author: auko on 2019-10-10 10:19
     * @Description: 未找到文件, 返回404
     */
    public void sendError() {
        sendHeader(404, "text/html", null);
    }

    /**
     * @Param: [url]
     * @Return: void
     * @Author: auko on 2019-10-10 22:07
     * @Description: 重定向到指定url
     */
    public void sendRedirect(String url){
        sendHeader(302, "text/html", null, 0,url,0);
    }
}
