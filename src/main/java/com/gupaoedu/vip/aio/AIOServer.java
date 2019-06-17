package com.gupaoedu.vip.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AIOServer {

    private final int port;

    public AIOServer(int port) {
        this.port = port;
        this.listen();
    }

    private void listen() {

        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService,1);
//            开门营业
//            参数中需要添加工作线程，用来监听回调，时间响应的时候需要回调
            final AsynchronousServerSocketChannel server= AsynchronousServerSocketChannel.open(threadGroup);
            server.bind(new InetSocketAddress(port));
            System.out.println("服务已启动，监听端口为："+port);
//          准备接收数据
            server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
                final ByteBuffer buffer = ByteBuffer.allocate(1024);
                @Override
                /**
                 * 实现completed方法，用来回调
                 * 由操作系统回调，当数据准备就绪后，就自动调用该方法
                 */
                public void completed(AsynchronousSocketChannel result, Object attachment) {

                    System.out.println("IO 操作成功，开始获取数据！");
                    buffer.clear();
                    try {
                        result.read(buffer).get();
                        buffer.flip();
                        result.write(buffer);
                        buffer.flip();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            result.close();
                            server.accept(null,this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("操作完成");
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.out.println("操作失败"+exc);

                }
            });

            try {
                Thread.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                System.out.println(e);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        int port = 8080;
        new AIOServer(port);
    }
}
