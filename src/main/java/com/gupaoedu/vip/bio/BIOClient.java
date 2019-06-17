package com.gupaoedu.vip.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class BIOClient {

    public static void main(String[] args) throws IOException {

//        服务器的IP和端口号
        Socket socket = new Socket("localhost",8080);
        OutputStream outputStream = socket.getOutputStream();
//        生成一个随机的ID
        String name = UUID.randomUUID().toString();

        System.out.println("客户端发送数据"+name);
        outputStream.write(name.getBytes());
        outputStream.flush();
        outputStream.close();
        socket.close();

    }

}
