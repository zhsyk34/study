package com.cat.aio2;

import com.cat.nio.base.Util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Archimedes on 2016/07/26.
 */
public class AsyncServerHandle implements Runnable {

    private CountDownLatch latch;
    private AsynchronousServerSocketChannel server;

    public AsyncServerHandle(int port) {

        try {
            server = AsynchronousServerSocketChannel.open();
            SocketAddress address = new InetSocketAddress(port);
            server.bind(address);

            System.out.println("server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        /**
         * block //TODO test
         */
        latch = new CountDownLatch(1);

        server.accept(this, new CompletionHandler<AsynchronousSocketChannel, AsyncServerHandle>() {
            @Override
            public void completed(AsynchronousSocketChannel channel, AsyncServerHandle attachment) {
                server.accept(attachment, this);
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                channel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        System.out.println("had receive data from client :" + Util.byteToStr(attachment));

                        ByteBuffer writeBuffer = ByteBuffer.wrap(new Date().toString().getBytes());
                        channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer attachment) {
                                if (attachment.hasRemaining()) {
                                    channel.write(attachment, attachment, this);
                                }
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {
                                Util.close(channel);
                            }
                        });
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        Util.close(channel);
                    }
                });
            }

            @Override
            public void failed(Throwable exc, AsyncServerHandle attachment) {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
