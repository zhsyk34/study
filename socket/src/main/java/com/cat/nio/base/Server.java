package com.cat.nio.base;

import com.cat.config.Config;

/**
 * Created by Archimedes on 2016/07/24.
 */
public class Server {

    public static void main(String[] args) {
        new Thread(new ServerHandle(Config.PORT)).start();
    }
}
