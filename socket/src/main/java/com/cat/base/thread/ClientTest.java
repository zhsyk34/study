package com.cat.base.thread;

import com.cat.config.Config;

/**
 * Created by Archimedes on 2016/07/23.
 */
public class ClientTest {

    public static void main(String[] args) {
        Client client1 = new Client(Config.HOST, Config.PORT) {

            @Override
            protected void onDisconnect(Transceiver transceiver) {
                System.out.println("disconnect");
            }

            @Override
            protected void onReceive(Transceiver transceiver, String message) {
                System.out.println("server data : " + message);
            }
        };

        Client client2 = new Client(Config.HOST, Config.PORT) {

            @Override
            protected void onDisconnect(Transceiver transceiver) {
                System.out.println("disconnect");
            }

            @Override
            protected void onReceive(Transceiver transceiver, String message) {
                System.out.println("receive server data : " + message);
            }
        };
        client1.connect();
        client2.connect();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Transceiver transceiver1 = client1.getTransceiver();
        Transceiver transceiver2 = client2.getTransceiver();

        for (int i = 1; i <= 10; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            transceiver1.send(Integer.toString(i));
            transceiver2.send(Integer.toString(i * 3));
        }

        client1.disconnect();
        client2.disconnect();
    }
}
