package com.cat.base.thread;

import com.cat.config.Config;

import java.util.Random;
import java.util.Scanner;

/**
 * Created by Archimedes on 2016/07/23.
 */
public class ClientTest2 {

    public static void main(String[] args) {
        Client client = new Client(Config.HOST, Config.PORT) {

            @Override
            protected void onDisconnect(Transceiver transceiver) {
                System.out.println("disconnect");
            }

            @Override
            protected void onReceive(Transceiver transceiver, String message) {
                System.out.println("receive server data : " + message);
            }
        };

        client.connect();

        while (!client.isConnect()) {
            System.out.println("wait for the transceiver");
        }
        Transceiver transceiver = client.getTransceiver();
        zero(transceiver);
    }

    private static void zero(Transceiver transceiver) {
        Random random = new Random();

        int i = random.nextInt(100);
        while (i > 20) {
            try {
                Thread.sleep(new Random().nextInt(50) * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//			System.out.println("client self:" + i);
            transceiver.send(">>>client data:" + Integer.toString(i));
            i = random.nextInt(100);
        }
        System.out.println("---------client end!----------");
    }

    private static void one(Transceiver transceiver) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            System.out.println("send : " + line);
            transceiver.send(line);
        }
    }

}
