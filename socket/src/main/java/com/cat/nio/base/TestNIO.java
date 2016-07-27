package com.cat.nio.base;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Archimedes on 2016/07/23.
 */
public class TestNIO {

    RandomAccessFile file;
    String encoding;

    @Before
    public void init() throws IOException {
        String root = this.getClass().getClassLoader().getResource("").getPath();
        file = new RandomAccessFile(root + "testnio.txt", "rw");
        encoding = System.getProperty("file.encoding");
        System.out.printf("path:%s\nsize:%d\n%s\n", root, file.length(), encoding);
    }

    @Test
    public void test1() throws IOException {
        FileChannel channel = file.getChannel();
        Util.print(channel);
    }

    @Test
    public void test2() throws IOException {
        FileChannel channel = file.getChannel();
        System.out.println(Util.read(channel));
    }

    @Test
    public void test3() throws IOException {
        FileChannel channel = file.getChannel();
        Util.write(channel, "So easy!");
    }

    @Test
    public void test4() throws IOException {
        FileChannel channel = file.getChannel();
        Util.write(channel, "\nEnd 结束了!", channel.size());
    }

    @Test
    public void test5() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.flip();
        System.out.println(buffer.remaining());
    }

    @Test
    public void test6() {
        String str = "Hello world";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        System.out.println(buffer.capacity());
        System.out.println(buffer.position());

        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get() + " ");
        }
    }

    @After
    public void distory() throws IOException {
        Util.close(file);
    }

}
