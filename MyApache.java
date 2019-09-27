package myApache;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * @packageName: myApache
 * @className: myApache
 * @Description: 自实现web服务器
 * @author: auko
 * @data 2019-09-27 8:46
 */
public class MyApache {
    public static void sop(Object obj) {
        System.out.println(obj);
    }

    Socket socket;
    static List<Socket> list = new ArrayList<>();
    static List<OutputStream> opsList = new ArrayList<OutputStream>();
    static List<DataOutputStream> dosList = new ArrayList<DataOutputStream>();
    static String rootUrl;
    static int port;

    public static void main(String argv[]) {
        try {
            ServerSocket serversocket = new ServerSocket(port);
            System.out.println("服务器启动...");
            init();
            send();
            while (true) {
                Socket socket = serversocket.accept();
                sop(socket.getRemoteSocketAddress());
                // 没接收到一个socket, 就新开一个receive线程和增加一个对应的输出流
                list.add(socket);
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
//                opsList.add(os);
                dosList.add(dos);
                MyApache server = new MyApache(socket);
            }
        } catch (RuntimeException e) {
            sop(e.getMessage());
        } catch (IOException e) {
            sop(e.getMessage());
        }

    }

    public MyApache(Socket socket) {
        this.socket = socket;
        receive();
    }

    /**
     * @Param: []
     * @Return: void
     * @Author: auko on 2019-09-27 9:51
     * @Description: 读取配置文件
     */
    public static void init() {
        try (
                FileInputStream fis = new FileInputStream("E:\\project\\java\\practice\\src\\myApache\\MyApache")
        ) {
            Properties pps = new Properties();
            pps.load(fis);
            sop("读取配置文件...");
            rootUrl = pps.getProperty("root");
            port = Integer.parseInt(pps.getProperty("port"));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @Param: []
     * @Return: void
     * @Author: auko on 2019-09-25 22:54
     * @Description: 服务器接收客户端信息
     */
    public void receive() {
        new Thread(() -> {
            try (
                    InputStream ins = socket.getInputStream();
                    DataInputStream dins = new DataInputStream(ins);
            ) {
                while (true) {
                    String msg = dins.readUTF();
                    System.out.println(msg);
                    // 根据当前线程名, 来区分不同客户端
                    int item = Integer.parseInt(Thread.currentThread().getName().split("-")[1]);
                    transfer(msg, item);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }


    /**
     * @Param: [msg, index]
     * @Return: void
     * @Author: auko on 2019-09-25 22:58
     * @Description: 服务端接收到信息后转发给各客户端
     */
    public static void transfer(String msg, int index) {
        // 转发时不发送给自己
        for (int i = 0; i < dosList.size(); i++) {
            try {
                if ((i + 1) != index) {
                    dosList.get(i).writeUTF(msg);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @Param: []
     * @Return: void
     * @Author: auko on 2019-09-25 22:58
     * @Description: 服务端广播给各个客户端
     */
    public static void send() {
        new Thread(() -> {
            Scanner scan = new Scanner(System.in);
            while (true) {
                System.out.println("服务器广播 : ");
                String msg = scan.nextLine();
                for (int i = 0; i < dosList.size(); i++) {
                    try {
                        dosList.get(i).writeUTF("服务器广播 : " + msg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}
