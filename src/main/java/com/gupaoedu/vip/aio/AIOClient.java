package com.gupaoedu.vip.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AIOClient {

    private final AsynchronousSocketChannel client;

    public AIOClient() throws IOException {
        this.client = AsynchronousSocketChannel.open();
    }

    public void connection(String host,int port){

        client.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Void>() {
            @Override
            public void completed(Void result, Void attachment) {
                client.write(ByteBuffer.wrap(("这是一条测试数据").getBytes()));
                System.out.println("已发送至服务端");
            }

            @Override
            public void failed(Throwable exc, Void attachment) {

                exc.printStackTrace();
            }
        });

        final ByteBuffer bb = ByteBuffer.allocate(1024);
        client.read(bb, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                System.out.println("IO操作完成"+result);
                System.out.println("获得反馈结果"+new String(bb.array()));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

                exc.printStackTrace();
            }
        });

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

    }


    public static void main(String[] args) {
        try {
            new AIOClient().connection("localhost",8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
