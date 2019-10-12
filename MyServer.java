package myServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import org.apache.log4j.Logger;
import java.net.InetAddress;

/**
 * @packageName: myServer
 * @className: myApache
 * @Description: 自实现web服务器
 * @author: auko
 * @data 2019-09-27 8:46
 */
public class MyServer {
    public String rootUrl;
    private String appUrl;
    private int port;

    public static void main(String argv[]) {
        MyServer server = new MyServer();
        server.start();
    }

    public Logger logger = new MyLogger(MyServer.class).logger;

    public MyServer() {
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
                InputStream fis = getClass().getResourceAsStream("/myServer/conf/myServer.properties")
        ) {
            Properties pps = new Properties();
            pps.load(fis);
            logger.info("read configuration");
            rootUrl = pps.getProperty("root");
            appUrl = rootUrl+"/webapps";
            port = Integer.parseInt(pps.getProperty("port"));
            if (rootUrl == null || port == 0) {
                logger.error("read MyServer configuration exception");
            }else{
                System.out.println("> root : "+rootUrl);
                System.out.println("> webServer : start");
            }

        } catch (FileNotFoundException e) {
            logger.error("can not find file MyServer.conf");
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
            logger.info("Server started : port "+port);
            System.out.println("App running at: ");
            System.out.println("- Local:   http://localhost:"+port);
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("- Network: http://"+addr.getHostAddress()+":"+port);
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
                if(req.getRequestValid()){
                    // 若该请求有效, 则进行处理
                    Response res = new Response(out);
                    Handler handler = new Handler(req, res, appUrl);
                    handler.requestHandler();
                }
                logger.trace("socket close : " + socket.getRemoteSocketAddress());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
