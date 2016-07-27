package com.cat.base.thread;

import com.cat.config.Config;

import java.util.Random;

/**
 * Created by Archimedes on 2016/07/23.
 */
public class ServerTest {

    private static boolean flag = false;

    public static void main(String[] args) {
        Server server = new Server(Config.PORT) {
            @Override
            protected void onConnect(Transceiver transceiver) {
                System.out.println(transceiver.getSocket().getInetAddress().getHostAddress() + " connect");
            }

            @Override
            protected void onDisconnect(Transceiver transceiver) {
                System.out.println("server disconnect!");
            }

            @Override
            protected void onReceive(Transceiver transceiver, String message) {
                System.out.println("receive client data : " + message);

                if (!flag) {
                    flag = true;
                    zero(transceiver);
                }
//				transceiver.send("Hello,I have receive your send data : " + message);
            }
        };
        server.start();

//		while (true) {
//			if (server.isStart()) {
//				break;
//			}
//		}
    }

    private static void zero(Transceiver transceiver) {
        Random random = new Random();

        int i = random.nextInt(100);
        while (i > 30) {
            try {
                Thread.sleep(new Random().nextInt(50) * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            transceiver.send(">>>>server data:" + Integer.toString(i));
            i = random.nextInt(100);
        }
        System.out.println("---------server end!----------");
    }
}
