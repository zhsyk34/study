package com.cat.base.thread;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Archimedes on 2016/07/22.
 */
public abstract class Client {

    private String host;

    private int port;

    private volatile boolean connect;

    private Transceiver transceiver;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try {
            Thread.sleep(1000);//TODO TEST
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Handle()).start();
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (this.transceiver != null) {
            this.transceiver.stop();
            this.transceiver = null;
        }
    }

    public boolean isConnect() {
        return this.connect;
    }

    public Transceiver getTransceiver() {
        return this.connect ? this.transceiver : null;
    }

    /**
     * 连接前
     */
    protected void beforeConnect() {
        System.out.println("client:after connect.");
    }

    /**
     * 连接后
     */
    protected void afterConnect() {
        System.out.println("client:after connect.");
    }

    /**
     * 断开连接
     */
    protected abstract void onDisconnect(Transceiver transceiver);

    /**
     * 连接异常
     */
    protected void connectException(Throwable throwable) {
        System.out.println("client connect error" + throwable.getMessage());
    }

    /**
     * 处理收到的数据
     */
    protected abstract void onReceive(Transceiver transceiver, String message);

    private class Handle implements Runnable {
        @Override
        public void run() {
            try {
                Client.this.beforeConnect();
                Socket socket = new Socket(host, port);

                transceiver = new Transceiver(socket) {
                    @Override
                    protected void onReceive(String message) {
                        Client.this.onReceive(this, message);
                    }

                    @Override
                    protected void onDisconnect() {
                        Client.this.connect = false;
                        Client.this.onDisconnect(this);
                    }
                };
                Client.this.transceiver.start();
                Client.this.connect = true;
                Client.this.afterConnect();
            } catch (IOException e) {
                e.printStackTrace();
                Client.this.connectException(e);
            }
        }
    }
}
