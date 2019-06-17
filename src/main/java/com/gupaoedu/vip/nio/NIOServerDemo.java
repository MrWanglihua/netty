package com.gupaoedu.vip.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServerDemo {

    //    准备两个东西，轮询器Selector，缓存区Buffer
//    轮询器 Selector      "大堂经理"
    private Selector selector;
    //    缓冲区，Buffer       "等候区"
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private int port = 8080;

    /**
     * 初始化轮询器
     *
     * @param port
     */
    public NIOServerDemo(int port) {
//        初始化大堂经理，开门营业
        try {
            this.port = port;
            ServerSocketChannel server = ServerSocketChannel.open();
//            告诉客户地址，开始接客
            server.bind(new InetSocketAddress(this.port));
//            由于NIO是BIO的升级版，为了兼容BIO，NIO默认设置为阻塞式
            server.configureBlocking(false);

//            大堂经理，准备接客
            selector = Selector.open();

//            大堂经理与营业厅确认，准备就绪，开始接客
            server.register(selector, SelectionKey.OP_ACCEPT);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 轮询的主线程
     */
    public void listen() {
        System.out.println("listen on" + this.port + ".");
        try {
//        轮询的主线程
            while (true) {

                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();
//                不断的轮询，即为迭代
                Iterator<SelectionKey> iterator = keys.iterator();
//                同步体现在这里，因为每次只能拿到一个key,每次只处理一个状态
                while (iterator.hasNext()){
//                    每一个key，代表一种状态
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    process(key);
                }
                

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 针对每一种状态给一个反应
     * @param key
     */
    private void process(SelectionKey key) throws IOException{

        if(key.isAcceptable()){

            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel channel = server.accept();

            channel.configureBlocking(false);
//            当数据准备就绪的时候，将状态改为可读
            channel.register(selector,SelectionKey.OP_READ);

        }else if(key.isReadable()){
//            key.channel()从多路复用器中拿到客户端的引用
            SocketChannel channel = (SocketChannel) key.channel();

            int len = channel.read(buffer);
            if(len>0){
                buffer.flip();

                String msg = new String(buffer.array(),0,len);

                key = channel.register(selector,SelectionKey.OP_WRITE);
//                在key上携带一个附件，一会再写出去
                key.attach(msg);
                System.out.println("读取内容"+msg);
            }


        }else if (key.isWritable()){
            SocketChannel channel = (SocketChannel) key.channel();
            String msg = (String) key.attachment();
            channel.write(ByteBuffer.wrap(("输出"+msg).getBytes()));

            channel.close();
        }
    }


    public static void main(String[] args) {

        new NIOServerDemo(8080).listen();
    }

}
