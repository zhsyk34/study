package com.cat.base.simple;

import com.cat.config.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Archimedes on 2016/07/22.
 */
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(Config.PORT);
        System.out.println("server start...");

        while (true) {
            Socket client = server.accept();
            new Thread(new ServerHandle(client)).start();
        }
    }
}
