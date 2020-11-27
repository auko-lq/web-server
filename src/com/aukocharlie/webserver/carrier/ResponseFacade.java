package com.aukocharlie.webserver.carrier;

import com.aukocharlie.webserver.enums.ExceptionLevelEnum;
import com.aukocharlie.webserver.enums.FileSuffixEnum;
import com.aukocharlie.webserver.enums.HttpStatusEnum;
import com.aukocharlie.webserver.exception.BaseException;
import com.aukocharlie.webserver.exception.FileSuffixUnmanagedException;
import com.aukocharlie.webserver.exception.StatusCodeUnmanagedException;
import com.aukocharlie.webserver.logger.LoggerTemplate;

import java.io.File;

/**
 * @author auko
 * @date 2020-01-27 22:07
 */
public class ResponseFacade extends LoggerTemplate {
    private Response response;

    public ResponseFacade(Response response) {
        this.response = response;
    }

    public void sendError(int statusCode) {
        HttpStatusEnum httpStatusEnum = HttpStatusEnum.valueOf(statusCode);
        if (httpStatusEnum == null) {
            throw new StatusCodeUnmanagedException("Existing unmanaged status code : " + statusCode);
        } else {
            response.setStatusCode(httpStatusEnum)
                    .setAcceptRanges("bytes")
                    .setContentType(FileSuffixEnum.HTML.getContentType())
                    .setDate()
                    .send();
        }
    }

    public void sendRedirect(String location) {
        response.setStatusCode(HttpStatusEnum.FOUND)
                .setAcceptRanges("bytes")
                .setContentType(FileSuffixEnum.HTML.getContentType())
                .setLocation(location)
                .setDate()
                .send();
    }

    public void sendStaticFile(File file) {
        if (file.exists() && file.canRead() && file.isFile()) {
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
            logger.debug("static file suffix : " + suffix);
            FileSuffixEnum fileSuffixEnum = FileSuffixEnum.getEnum(suffix);
            if (fileSuffixEnum == null) {
                sendError(500);
                throw new FileSuffixUnmanagedException("can't handle the suffix : " + suffix);
            } else {
                response.setStatusCode(HttpStatusEnum.OK)
                        .setAcceptRanges("bytes")
                        .setKeepAlive()
                        .setContentType(fileSuffixEnum.getContentType())
                        .setContentLength(file.length())
                        .setDate()
                        .setLastModified(file.lastModified())
                        .setFile(file)
                        .send();
            }
        } else {
            sendError(404);
            throw new BaseException(ExceptionLevelEnum.INFO, "Static file does not exist or file can't read : " + file.getName(), new RuntimeException());
        }
    }

}
