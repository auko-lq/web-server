package com.aukocharlie.webserver.carrier;

import java.util.Map;

/**
 * @author auko
 * @date 2020-01-27 22:07
 */
public class RequestFacade {
    private Request request;

    public RequestFacade(Request request) {
        this.request = request;
    }

    public boolean isValid() {
        return request.isValid();
    }

    public String getMethod() {
        return request.getMethod();
    }

    public String getRequestURI() {
        return request.getRequestURI();
    }

    public boolean isKeepAlive() {
        return request.isKeepAlive();
    }

    public int getLocalPort() {
        return request.getLocalPort();
    }

    public int getPort() {
        return request.getPort();
    }

    public String getQueryString() {
        return request.getQueryString();
    }

    public String getParameter(String key) {
        return request.getParameterMap().get(key);
    }

    public Map getParameterMap() {
        return request.getParameterMap();
    }
}
