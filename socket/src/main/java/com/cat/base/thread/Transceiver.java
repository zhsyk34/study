package com.cat.base.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Archimedes on 2016/07/22.
 * 收发器
 */
public abstract class Transceiver {

    private Socket socket;

    private volatile boolean active;

    private DataInputStream input;

    private DataOutputStream output;

    public Transceiver(Socket socket) {
        this.socket = socket;
    }

    public final void start() {
        this.active = true;
        new Thread(new Handle()).start();
    }

    public final Socket getSocket() {
        return socket;
    }

    /**
     * 主动关闭
     */
    public final void stop() {
        this.active = false;
    }

    public final boolean send(String message) {
        if (active) {
            try {
                output.writeUTF(message);
                output.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 断开连接
     */
    protected abstract void onDisconnect();

    /**
     * 处理收到的数据
     */
    protected abstract void onReceive(String message);

    private class Handle implements Runnable {

        @Override
        public void run() {
            /**
             * init
             */
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                active = false;
            }

            /**
             * do
             */
            while (active) {
                try {
                    Transceiver.this.onReceive(input.readUTF());
                } catch (IOException e) {
                    e.printStackTrace();
                    active = false;
                }

            }

            /**
             * release
             */
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /**
             * listen
             */
            onDisconnect();
        }
    }

}
