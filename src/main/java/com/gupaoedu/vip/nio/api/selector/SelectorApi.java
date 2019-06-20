package com.gupaoedu.vip.nio.api.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorApi {/*
    int port;

    *//*
     * 注册事件
     *//*
    private Selector getSelector() throws IOException {
// 创建 Selector 对象
        Selector sel = Selector.open();
// 创建可选择通道，并配置为非阻塞模式
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
// 绑定通道到指定端口
        ServerSocket socket = server.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        socket.bind(address);
// 向 Selector 中注册感兴趣的事件
        server.register(sel, SelectionKey.OP_ACCEPT);
        return sel;
    }

    *//*
     * 开始监听
     *//*
    public void listen() {
        System.out.println("listen on " + port);
        try {
            while (true) {
// 该调用会阻塞，直到至少有一个事件发生
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = (SelectionKey) iter.next();
                    iter.remove();
                    process(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    *//*
     * 根据不同的事件做处理
     *//*
    private void process(SelectionKey key) throws IOException{
// 接收请求
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel channel = server.accept();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
        }
// 读信息
        else if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            int len = channel.read(buffer);
            if (len > 0) {
                buffer.flip();
                content = new String(buffer.array(),0,len);
                SelectionKey sKey = channel.register(selector, SelectionKey.OP_WRITE);
                sKey.attach(content);
            } else {
                channel.close();
            }
            buffer.clear();
        }
// 写事件
        else if (key.isWritable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            String content = (String) key.attachment();
            ByteBuffer block = ByteBuffer.wrap(("输出内容：" + content).getBytes());
            if(block != null){
                channel.write(block);
            }else{
                channel.close();
            }
        }
    }*/

}
