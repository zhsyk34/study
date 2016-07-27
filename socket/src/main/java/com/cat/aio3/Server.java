package com.cat.aio3;

import static com.cat.config.Config.PORT;

/**
 * Created by Archimedes on 2016/07/24.
 */
public class Server {

    public static void main(String[] args) {
        new Thread(new AsyncServerHandle(PORT)).start();
    }
}
