package com.cat.nio.base;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Archimedes on 2016/07/24.
 */
public class ClientHandle implements Runnable {

    private String host;
    private int port;

    private Selector selector;
    private SocketChannel socketChannel;

    private volatile boolean stop;

    public ClientHandle(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!stop) {
            try {
                int ready = selector.select(1000);
                if (ready == 0) {
                    continue;
                }

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

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
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        Util.close(selector);
    }

    private void connect() throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(host, port);

        boolean connected = socketChannel.connect(serverAddress);

        if (connected) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            write(socketChannel, "client message");
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void handle(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            return;
        }

        SocketChannel channel = (SocketChannel) key.channel();
        if (key.isConnectable()) {
            if (channel.finishConnect()) {
                channel.register(selector, SelectionKey.OP_READ);
                write(channel, "client message");
            } else {
                System.exit(1);
            }
        }
        if (key.isReadable()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            buffer.flip();
            if (read > 0) {
                String message = Util.byteToStr(buffer);
                System.out.println(message);
                this.stop = true;
            } else if (read < 0) {
                key.cancel();
                channel.close();
            } else {
                //
            }
        }
    }

    private void write(SocketChannel channel, String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        channel.write(buffer);
        if (!buffer.hasRemaining()) {
            System.out.println("Send order 2 server succeed.");
        }
    }

}
