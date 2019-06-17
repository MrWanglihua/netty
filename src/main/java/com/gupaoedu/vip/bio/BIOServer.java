package com.gupaoedu.vip.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {


    ServerSocket serverSocket;

    public BIOServer(int  port) {
        try {
//            Tomcat 端口号为8080
//            mysql 端口号为3306
//            Redis   6379
//            Zookeeper 2181
            serverSocket = new ServerSocket(port);
            System.out.println("BIOServer 服务已启动，端口号为"+port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始监听，并处理逻辑
     */
    public void listen(){
//        循环监听
        while (true){
            try {
//                等待客户端连接，阻塞方法，
//                Socket 为数据发送端在服务端的引用
                Socket socket = serverSocket.accept();
                System.out.println(socket.getPort());
//              读取数据
                InputStream inputStream = socket.getInputStream();

                byte[] buff = new byte[1024];
                int len = inputStream.read(buff);
                if(len>0){

                    String msg = new String(buff,0,len);
                    System.out.println("收到"+msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new BIOServer(8080).listen();
    }

}
