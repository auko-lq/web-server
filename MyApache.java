package myApache;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * @packageName: myApache
 * @className: myApache
 * @Description: 自实现web服务器
 * @author: auko
 * @data 2019-09-27 8:46
 */
public class MyApache {
    private String rootUrl;
    private int port;

    public static void main(String argv[]) {
        MyApache apache = new MyApache();
        apache.start();
    }

    public Logger logger = new MyLogger(MyApache.class).logger;

    public MyApache() {
        init();
    }

    /**
     * @Param: []
     * @Return: void
     * @Author: auko on 2019-09-27 9:51
     * @Description: 读取配置文件
     */
    public void init() {
        try (
                FileInputStream fis = new FileInputStream("src/myApache/conf/MyApache.properties")
        ) {
            Properties pps = new Properties();
            pps.load(fis);
            logger.info("read configuration");
            rootUrl = pps.getProperty("root");
            port = Integer.parseInt(pps.getProperty("port"));
            if (rootUrl == null || port == 0) {
                logger.error("read MyApache configuration exception");
            }

        } catch (FileNotFoundException e) {
            logger.error("can not find file MyApache.conf");
        } catch (IOException e) {
            logger.error("IOException : " + e.getMessage());
        }

    }


    /**
     * @Param: []
     * @Return: void
     * @Author: auko on 2019-10-09 8:35
     * @Description: 启动服务器, 等待连接
     */
    public void start() {
        try {
            ServerSocket serversocket = new ServerSocket(port);
            logger.info("Server started");
            while (true) {
                Socket socket = serversocket.accept();
                // new thread 处理客户请求
                service(socket);
            }
        } catch (RuntimeException e) {
            logger.warn("RuntimeException : " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException : " + e.getMessage());
        }
    }


    /**
     * @Param: [socket]
     * @Return: void
     * @Author: auko on 2019-10-09 12:05
     * @Description: 处理客户端请求
     */
    public void service(Socket socket) {
        new Thread(() -> {
            try (
                    InputStream ins = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();
            ) {
                logger.trace("receive socket : " + socket.getRemoteSocketAddress());
                Request req = new Request(ins);
                Response res = new Response(out);

                Handler handler = new Handler(req, res, rootUrl);
                handler.requestHandler();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
