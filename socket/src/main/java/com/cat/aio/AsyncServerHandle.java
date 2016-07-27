package com.cat.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Archimedes on 2016/07/24.
 */
public class AsyncServerHandle implements Runnable {

    CountDownLatch latch;

    AsynchronousServerSocketChannel server;

    public AsyncServerHandle(int port) {
        InetSocketAddress address = new InetSocketAddress(port);
        try {
            server = AsynchronousServerSocketChannel.open();
            server.bind(address);

            System.out.println("server start in port :　" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        server.accept(this, new AcceptCompletionHandler());

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
