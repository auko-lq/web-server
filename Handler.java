package myServer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import java.io.File;


/**
 * @packageName: myServer
 * @className: Handler
 * @Description: 处理机制
 * @author: auko
 * @data 2019-10-09 15:45
 */
public class Handler {
    private Request req;
    private Response res;
    private String rootUrl;
    private String url;
    String queryString = null;


    public Logger logger = new MyLogger(Handler.class).logger;

    public Handler(Request req, Response res, String rootUrl) {
        this.req = req;
        this.res = res;
        this.rootUrl = rootUrl;
    }

    public void requestHandler() {
        // 获取到uri
        String uri = req.getUri();


        // 若只有 "/" 则重定向到index.html, 即默认打开index.html
        if (uri != null && uri.length() == 1) {
            res.sendRedirect("/index.html");
        }
        url = rootUrl + uri;

        // remove query string
        url = removeQueryString(url);

        File file = new File(url);

        // 如果存在 && 可读  则找出后缀名
        int index2 = url.lastIndexOf('.'); // 便于分割文件名与后缀
        if (file.exists() && file.canRead() && index2 > -1) {
            // ready to send
            readySend(file, url, index2);
        } else {
            // 如果正常方式找不到文件则, 判断是否为unicode编码, 解码后再尝试寻找文件
            // 找到文件名
            int index3 = url.lastIndexOf('/');
            String fileName;
            String decodedFileName = "";
            if (index2 > index3) {
                // 如果最后的 '.' 在最后的 '/' 之后, 则可以分割文件名
                fileName = url.substring(index3, index2);
                try {
                    decodedFileName = URLDecoder.decode(fileName, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    logger.warn("unsupported encoding fileName : " + fileName);
                }
                String decodedUrl = url.substring(0, index3) + decodedFileName + url.substring(index2);
                logger.trace("try to decode fileName : " + decodedUrl);
                file = new File(decodedUrl);
                if (file.exists() && file.canRead()) {
                    // ready to send
                    readySend(file, url, index2);
                } else {
                    // 不存在 或者 不可读, 返回404
                    res.sendError();
                }
            } else {
                // '.' 在 '/' 前面 说明该url缺少后缀, 则尝试在后面加上html
                String addSuffixUrl = url + ".html";
                file = new File(addSuffixUrl);
                if (file.exists() && file.canRead()) {
                    // 若加上后缀名后能找到文件, 则重定向到该文件
                    res.sendRedirect(uri + ".html");
                } else {
                    // 否则返回404
                    res.sendError();
                }
            }
        }
    }

    /**
     * @Param: [url]
     * @Return: java.lang.String
     * @Author: auko on 2019-10-10 23:20
     * @Description: 移除query String, 返回url
     */
    public String removeQueryString(String url) {
        int index1 = url.indexOf('?');
        if (index1 > -1) {
            queryString = url.substring(index1 + 1);
            url = url.substring(0, index1);
            System.out.println("queryString : " + queryString);
        }
        return url;
    }

    /**
     * @Param: [file, suffix, index]
     * @Return: void
     * @Author: auko on 2019-10-10 23:23
     * @Description: 发送回复  index 为文件名与后缀名间的 '.'的下标
     */
    public void readySend(File file, String url, int index) {
        String suffix = url.substring(index + 1);
        System.out.println("suffix : " + suffix);
        switch (suffix) {
            case "html":
                res.sendFile(file, "text/html");
                break;
            case "ico":
                res.sendPic(file, "image/vnd.microsoft.icon");
                break;
            case "png":
                res.sendPic(file, "image/png");
                break;
            case "jpg":
                res.sendPic(file, "image/jpeg");
                break;
            case "js":
                res.sendFile(file, "text/javascript");
                break;
            case "css":
                res.sendFile(file, "text/css");
                break;
            default:
                logger.warn("can't handle the suffix : " + suffix);
        }
    }
}
