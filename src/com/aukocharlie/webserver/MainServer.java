package com.aukocharlie.webserver;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.aukocharlie.webserver.carrier.Request;
import com.aukocharlie.webserver.carrier.RequestFacade;
import com.aukocharlie.webserver.carrier.Response;
import com.aukocharlie.webserver.carrier.ResponseFacade;
import com.aukocharlie.webserver.config.ServerConfiguration;
import com.aukocharlie.webserver.enums.ExceptionLevelEnum;
import com.aukocharlie.webserver.exception.BaseException;
import com.aukocharlie.webserver.handler.StaticResourcesHandler;
import com.aukocharlie.webserver.logger.LoggerTemplate;

/**
 * main entrance
 */
public class MainServer extends LoggerTemplate {

    public static void main(String[] argv) {
        MainServer server = new MainServer();
    }

    private ServerConfiguration config;

    public MainServer() {
        try {
            init();
            start();
        } catch (BaseException baseException) {
            if (baseException.getLevel() == ExceptionLevelEnum.DEBUG) {
                logger.debug(baseException.getMessage());
            } else if (baseException.getLevel() == ExceptionLevelEnum.INFO) {
                logger.info(baseException.getMessage());
            } else if (baseException.getLevel() == ExceptionLevelEnum.WARN) {
                logger.warn(baseException.getMessage());
            } else if (baseException.getLevel() == ExceptionLevelEnum.ERROR) {
                logger.error(baseException.getMessage(),baseException);
            }
        } catch (Exception exception) {
            logger.warn("unmanaged exception");
        }
    }

    /**
     * initialize server configuration
     */
    public void init() {
        config = ServerConfiguration.getInstance("conf/server.xml");
        logger.info("> Context : " + config.getContextPath());
        logger.info("> WebServer : start");
    }


    /**
     * turn on the server
     */
    public void start() {
        try {
            ServerSocket serversocket = new ServerSocket(config.getPort());
            logger.info("Server started : port " + config.getPort());
            logger.info("App running at: ");
            logger.info(" - Local:   http://localhost:" + config.getPort());
            InetAddress addr = InetAddress.getLocalHost();
            logger.info(" - Network: http://" + addr.getHostAddress() + ":" + config.getPort());
            while (true) {
                Socket socket = serversocket.accept();
                service(socket);
            }
        } catch (RuntimeException e) {
            throw new BaseException(ExceptionLevelEnum.WARN, "RuntimeException : " + e.getMessage(), e);
        } catch (IOException e) {
            throw new BaseException(ExceptionLevelEnum.ERROR, "IOException : " + e.getMessage(), e);
        }
    }


    /**
     * create a new service thread to handle request
     *
     * @param socket server socket
     */
    public void service(Socket socket) {
        new Thread(() -> {
            try (
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
            ) {
                logger.debug("receive socket : " + socket.getRemoteSocketAddress());
                StaticResourcesHandler handler = new StaticResourcesHandler(
                        new RequestFacade(new Request(inputStream, socket)),
                        new ResponseFacade(new Response(outputStream)));
                handler.handle();
                logger.debug("socket close : " + socket.getRemoteSocketAddress());
            } catch (BaseException baseException) {
                if (baseException.getLevel() == ExceptionLevelEnum.DEBUG) {
                    logger.debug(baseException.getMessage());
                } else if (baseException.getLevel() == ExceptionLevelEnum.INFO) {
                    logger.info(baseException.getMessage());
                } else if (baseException.getLevel() == ExceptionLevelEnum.WARN) {
                    logger.warn(baseException.getMessage());
                } else if (baseException.getLevel() == ExceptionLevelEnum.ERROR) {
                    logger.error(baseException.getMessage(),baseException);
                }
            } catch (IOException e) {
                throw new BaseException(ExceptionLevelEnum.ERROR, "IOException : " + e.getMessage(), e);
            }
        }).start();
    }
}
