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
public class Client {

    public static void main(String[] args) throws IOException {
        try {
            Socket socket = new Socket(Config.HOST, Config.PORT);
            System.out.println("client start ...");

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line = input.readLine();
            while (!line.equals(Config.EXIT)) {
                System.out.println("send : " + line);
                writer.println(line);
                writer.flush();

                line = input.readLine();
            }

            writer.close();
            reader.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("can not listen to:" + e);
        }
    }
}
