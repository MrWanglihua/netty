package com.gupaoedu.vip.nio.api;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 直接缓冲区
 */
public class DirectBuffer {
    static public void main(String args[]) throws Exception {
//首先我们从磁盘上读取刚才我们写出的文件内容
        String infile = "E://data//test.txt";
        FileInputStream fin = new FileInputStream(infile);
        FileChannel fcin = fin.getChannel();
//把刚刚读取的内容写入到一个新的文件中
        String outfile = String.format("E://testcopy.txt");
        FileOutputStream fout = new FileOutputStream(outfile);
        FileChannel fcout = fout.getChannel();
// 使用 allocateDirect，而不是 allocate
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (true) {
            buffer.clear();
            int r = fcin.read(buffer);
            if (r == -1) {
                break;
            }
            buffer.flip();
            fcout.write(buffer);
        }
    }
}