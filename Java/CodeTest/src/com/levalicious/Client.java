package com.levalicious;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class Client extends Thread {

    static Socket client1;
    static int[][] cards = new int[4][4];
    static boolean upDown[][] = new boolean[4][4];
    private ServerSocket s;

    public Client(ServerSocket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            client1 = s.accept();
            ObjectInputStream  in =   new ObjectInputStream (client1.getInputStream());
            ObjectOutputStream os = new ObjectOutputStream(client1.getOutputStream());
            DataInputStream reader = new DataInputStream(client1.getInputStream());
            DataOutputStream writer = new DataOutputStream(client1.getOutputStream());

            //recieve 2d arrays
            int[][] cards1 = (int[][]) in.readObject();
            boolean[][] upDown1 = (boolean[][]) in.readObject();

            String intro = reader.readUTF();

            System.out.println(intro);
// creates table for player
            System.out.println("     1 2 3 4 ");
            System.out.println("---+---------");
            for (int i = 0; i < 4; i++) {
                System.out.print(" " + (i + 1) + " | ");
                for (int a = 0; a < 4; a++) {
                    if (upDown1[i][a]) {
                        System.out.print(cards1[i][a]);
                        System.out.print(" ");
                    }
                    else
                        System.out.print("* ");
                }
                System.out.println();
            }
            System.out.println();
        } catch(Exception e) {

        }
    }
}