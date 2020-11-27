package com.aukocharlie.webserver.carrier;

import com.aukocharlie.webserver.constant.HttpProtocol;
import com.aukocharlie.webserver.constant.ResponseHeaders;
import com.aukocharlie.webserver.enums.ExceptionLevelEnum;
import com.aukocharlie.webserver.enums.HttpStatusEnum;
import com.aukocharlie.webserver.exception.BaseException;
import com.aukocharlie.webserver.logger.LoggerTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 回复类
 *
 * @author auko
 * @date 2019-10-09 14:38
 */
public class Response extends LoggerTemplate {
    private OutputStream outputStream;

    private HttpStatusEnum statusCode;
    private StringBuffer headers = new StringBuffer();
    private File file;
    private String text;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Response setStatusCode(HttpStatusEnum statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Response setAcceptRanges(String acceptRanges) {
        headers.append(String.format("%s: %s\n", ResponseHeaders.ACCEPT_RANGES, acceptRanges));
        return this;
    }

    public Response setKeepAlive() {
        headers.append(String.format("%s: %s\n", ResponseHeaders.CONNECTION, ResponseHeaders.KEEP_ALIVE));
        return this;
    }

    public Response setContentType(String contentType) {
        headers.append(String.format("%s: %s\n", ResponseHeaders.CONTENT_TYPE, contentType));
        return this;
    }

    public Response setContentLength(long contentLength) {
        headers.append(String.format("%s: %d\n", ResponseHeaders.CONTENT_LENGTH, contentLength));
        return this;
    }

    public Response setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
        headers.append(String.format("%s: %s\n", ResponseHeaders.DATE, sdf.format(new Date())));
        return this;
    }

    public Response setLocation(String location) {
        headers.append(String.format("%s: %s\n", ResponseHeaders.LOCATION, location));
        return this;
    }

    public Response setLastModified(long lastModified) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
        headers.append(String.format("%s: %s\n", ResponseHeaders.LAST_MODIFIED, sdf.format(lastModified)));
        return this;
    }

    public Response setHeaders(String key, String value) {
        headers.append(String.format("%s: %s\n", key, value));
        return this;
    }

    public Response setFile(File file) {
        this.file = file;
        return this;
    }

    public Response setText(String text) {
        this.text = text;
        return this;
    }

    public void send() {
        try {
            outputStream.write(String.format("%s %d %s\n",
                    HttpProtocol.HTTP_1_1,
                    this.statusCode.getStatus(),
                    this.statusCode.getDescription())
                    .getBytes());
            outputStream.write((headers.toString() + "\n").getBytes());
            if (null != file) {
                sendFile(file);
            }else if(null != text && text.equals("")){
                outputStream.write(text.getBytes());
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new BaseException(ExceptionLevelEnum.ERROR, "IOException : " + e.getMessage(), e);
        }
    }

    /**
     * 发送文件
     *
     * @param file 文件
     */
    private void sendFile(File file) {
        try (
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis)
        ) {
            byte[] b = new byte[1024];
            int len;
            while ((len = bis.read(b)) != -1) {
                outputStream.write(b, 0, len);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new BaseException(ExceptionLevelEnum.ERROR, "IOException : " + e.getMessage(), e);
        }
    }
}
