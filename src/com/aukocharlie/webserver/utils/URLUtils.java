package com.aukocharlie.webserver.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * URL 工具类
 */
public class URLUtils {
    /**
     * URL解码
     *
     * @param url 解码码前URL
     * @return 解码后的URL
     * @throws UnsupportedEncodingException
     */
    public static String decodeURL(String url) throws UnsupportedEncodingException {
        String prevURL = "";
        String decodeURL = url;
        // 循环解码
        while (!prevURL.equals(decodeURL)) {
            prevURL = decodeURL;
            decodeURL = URLDecoder.decode(decodeURL, "UTF-8");
        }
        return decodeURL;
    }
}
