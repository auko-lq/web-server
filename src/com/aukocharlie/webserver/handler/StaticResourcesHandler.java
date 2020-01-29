package com.aukocharlie.webserver.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.aukocharlie.webserver.carrier.RequestFacade;
import com.aukocharlie.webserver.carrier.ResponseFacade;
import com.aukocharlie.webserver.config.ServerConfiguration;
import com.aukocharlie.webserver.enums.ExceptionLevelEnum;
import com.aukocharlie.webserver.exception.BaseException;
import com.aukocharlie.webserver.logger.LoggerTemplate;

import java.io.File;


/**
 * 静态资源处理器
 */
public class StaticResourcesHandler extends LoggerTemplate {
    private RequestFacade requestFacade;
    private ResponseFacade responseFacade;
    private ServerConfiguration config;


    public StaticResourcesHandler(RequestFacade requestFacade, ResponseFacade responseFacade) {
        this.requestFacade = requestFacade;
        this.responseFacade = responseFacade;
        this.config = ServerConfiguration.getInstance();
    }

    /**
     * 处理请求
     */
    public void handle() {
        // 检查request是否有效
        if (!requestFacade.isValid()) {
            responseFacade.sendError(400);
        } else {
            String requestURI = requestFacade.getRequestURI();

            // 若只有 "/" 则重定向到index.html, 即默认打开index.html
            if (requestURI != null && requestURI.equals("/")) {
                responseFacade.sendRedirect("/index.html");
            }

            int index1 = requestURI.lastIndexOf('/');
            int index2 = requestURI.lastIndexOf('.');

            if (index1 > index2) {
                // '/'在'.'后面, 说明URI所指文件不存在后缀名, 则自动重定向到html为后缀
                responseFacade.sendRedirect(requestURI + ".html");
            } else {
                // 先判断文件是否存在
                File tempFile = new File(config.getContextPath() + requestURI);
                if (tempFile.exists() && tempFile.isFile()) {
                    // 正常
                    responseFacade.sendStaticFile(tempFile);
                } else {
                    // 尝试对文件名进行解码
                    String fileName = requestURI.substring(index1 + 1, index2);
                    String decodedFileName;
                    try {
                        decodedFileName = URLDecoder.decode(fileName, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new BaseException(ExceptionLevelEnum.WARN, "can't decode the file name : " + fileName, e);
                    }
                    tempFile = new File(config.getContextPath() +
                            requestURI.substring(0, index1 + 1) +
                            decodedFileName +
                            requestURI.substring(index2));
                    responseFacade.sendStaticFile(tempFile);
                }
            }
        }
    }

//            // 如果正常方式找不到文件则, 判断是否为unicode编码, 解码后再尝试寻找文件
//            // 找到文件名
//            int index3 = requestURI.lastIndexOf('/');
//            String fileName;
//            String decodedFileName = "";
//            if (index2 > index3) {
//                // 如果最后的 '.' 在最后的 '/' 之后, 则可以分割文件名
//                fileName = requestURI.substring(index3, index2);
//                try {
//                    decodedFileName = URLDecoder.decode(fileName, "utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    logger.warn("unsupported encoding fileName : " + fileName);
//                }
//                String decodedUrl = requestURI.substring(0, index3) + decodedFileName + requestURI.substring(index2);
//                logger.trace("try to decode fileName : " + decodedUrl);
}
