package com.cat.nio.base;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

/**
 * Created by Archimedes on 2016/07/23.
 */
public class Util {

    private static final String encoding = System.getProperty("file.encoding");

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            closeable = null;
        }
    }

    public static void print(ReadableByteChannel channel) {
        print(channel, 1024);
    }

    public static void print(ReadableByteChannel channel, int capacity) {
        ByteBuffer buffer = ByteBuffer.allocate(capacity);
        try {
            while (channel.read(buffer) > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String read(ReadableByteChannel channel) {
        return read(channel, 1024);
    }

    public static String read(ReadableByteChannel channel, int capacity) {
        ByteBuffer buffer = ByteBuffer.allocate(capacity);
        StringBuilder builder = new StringBuilder();
        try {
            while (channel.read(buffer) > 0) {
                buffer.flip();
                builder.append(byteToStr(buffer));
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static void write(WritableByteChannel channel, String str) {
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        try {
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(FileChannel channel, String str, long position) {
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        try {
            channel.write(buffer, position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRootPath() {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    public static String byteToStr(ByteBuffer buffer) {
        buffer.flip();
        return Charset.forName(encoding).decode(buffer).toString();
    }

    public static String byteToStrByBytes(ByteBuffer buffer) throws UnsupportedEncodingException {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        return new String(bytes, encoding);
    }
}
