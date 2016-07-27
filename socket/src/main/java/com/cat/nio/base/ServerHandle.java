package com.cat.nio.base;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Archimedes on 2016/07/24.
 */
public class ServerHandle implements Runnable {

    private int port;

    private volatile boolean stop;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public ServerHandle(int port) {
        this.port = port;

        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            InetSocketAddress address = new InetSocketAddress(port);
            serverSocketChannel.socket().bind(address, 1024);//最大连接数1024
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                SelectionKey key;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();

                    try {
                        handle(key);
                    } catch (IOException e) {
                        if (key != null) {
                            key.cancel();

                            Util.close(key.channel());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Util.close(selector);
    }

    private void handle(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            return;
        }

        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel channel = server.accept();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
        }

        if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();

            if (key.isReadable()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int read = channel.read(buffer);
                buffer.flip();

                if (read > 0) {
                    String message = Util.byteToStr(buffer);
                    System.out.println("server receive :" + message);

                    write(channel, "Hello client" + System.currentTimeMillis());
                } else if (read < 0) {
                    key.cancel();
                    channel.close();
                } else {
                    //
                }
            }
        }
    }

    private void write(SocketChannel channel, String message) throws IOException {
        if (message != null && message.trim().length() > 0) {
            byte[] bytes = message.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            channel.write(buffer);
        }
    }
}
