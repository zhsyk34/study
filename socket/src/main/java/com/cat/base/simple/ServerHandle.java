package com.cat.base.simple;

import com.cat.config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Archimedes on 2016/07/22.
 */
public class ServerHandle implements Runnable {

    private Socket client;

    public ServerHandle(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream());

            while (true) {
                String data = reader.readLine();
                if (Config.EXIT.equalsIgnoreCase(data)) {
                    break;
                }
                System.out.println("Client : " + data);
            }
            reader.close();
            writer.close();
            client.close();
        } catch (IOException e) {
            System.out.println("error : " + e.getMessage());
        }
    }
}
