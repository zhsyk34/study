package com.cat.aio3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Archimedes on 2016/07/26.
 */
public class AsyncClientHandle implements Runnable {

    private String host;
    private int port;
    private String message;

    private CountDownLatch latch;
    private AsynchronousSocketChannel client;

    public AsyncClientHandle(String host, int port, String message) {
        this.host = host;
        this.port = port;
        this.message = message;

        latch = new CountDownLatch(1);
        try {
            client = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        SocketAddress address = new InetSocketAddress(host, port);
        /**
         * connect
         */
        client.connect(address, this, new CompletionHandler<Void, AsyncClientHandle>() {
            @Override
            public void completed(Void result, AsyncClientHandle attachment) {
                ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes());
                client.write(writeBuffer, writeBuffer, new BufferWriter(client, true) {
                    @Override
                    protected void doAfter(AsynchronousSocketChannel channel) {
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        client.read(readBuffer, readBuffer, new BufferReader(client, true) {
                            @Override
                            protected void doAfter(AsynchronousSocketChannel channel) {
                                release(latch, client);
                            }
                        });
                    }
                });
            }

            @Override
            public void failed(Throwable exc, AsyncClientHandle attachment) {
                release(latch, client);
            }
        });

        /**
         * release
         */
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void release(CountDownLatch latch, AsynchronousSocketChannel channel) {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }
}
