package com.cat.aio;

import java.util.Random;

import static com.cat.config.Config.HOST;
import static com.cat.config.Config.PORT;

/**
 * Created by Archimedes on 2016/07/24.
 */
public class Client {

    public static void main(String[] args) {
        String message = "client message : " + new Random().nextInt(100);
        new Thread(new AsyncClientHandle(HOST, PORT, message), "client-one").start();
    }
}
