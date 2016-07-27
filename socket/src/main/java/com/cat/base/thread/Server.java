package com.cat.base.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archimedes on 2016/07/22.
 */
public abstract class Server {

    private final List<Transceiver> clients = new ArrayList<>();
    private int port;
    private volatile boolean start;

    public Server(int port) {
        this.port = port;
    }

    public final boolean isStart() {
        return this.start;
    }

    public final void start() {
        this.start = true;
        new Thread(new Handle()).start();
    }

    public void stop() {
        this.start = false;
    }

    /**
     * 服务端启动前
     */
    protected void beforeStart() {
        System.out.println("server:before start.");
    }

    /**
     * 服务端启动后
     */
    protected void afterStart() {
        System.out.println("server:after start.");
    }

    /**
     * 服务端启动异常
     */
    protected void startException(Throwable throwable) {
        System.out.println("server:start error," + throwable.getMessage());
    }

    /**
     * 服务端停止后
     */
    protected void afterStop() {
        System.out.println("server:stop!");
    }

    /**
     * 与客户端建立连接监听
     */
    protected abstract void onConnect(Transceiver transceiver);

    /**
     * 与客户端连接失败
     */
    protected void connectException(Throwable throwable) {
        System.out.println("client error : " + throwable.getMessage());
    }

    /**
     * 断开与客户端的连接
     */
    protected abstract void onDisconnect(Transceiver transceiver);

    /**
     * 处理接收到的数据
     */
    protected abstract void onReceive(Transceiver transceiver, String message);

    private void connect(Socket socket) {
        Transceiver client = new Transceiver(socket) {
            @Override
            protected void onDisconnect() {
                Server.this.clients.remove(this);
                Server.this.onDisconnect(this);
            }

            @Override
            protected void onReceive(String message) {
                Server.this.onReceive(this, message);
            }
        };
        client.start();
        Server.this.clients.add(client);
        Server.this.onConnect(client);
    }

    private class Handle implements Runnable {
        @Override
        public void run() {
            Server.this.beforeStart();
            ServerSocket server = null;
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
                Server.this.startException(e);
            }
            Server.this.afterStart();

            while (start) {
                try {
                    Socket client = server.accept();
                    Server.this.connect(client);
                } catch (IOException e) {
                    e.printStackTrace();
                    Server.this.connectException(e);
                }
            }

            /**
             * 关闭服务器
             */
            try {
                Server.this.clients.forEach(client -> client.stop());
                Server.this.clients.clear();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Server.this.afterStop();
        }
    }
}
