package com.levalicious;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server extends Thread{

    static ServerSocket server;
    static Socket client;
    static int[][] cards = new int[4][4];
    static boolean upDown[][] = new boolean[4][4];

    @Override
    public void run() {
        try{
            System.out.println("Server started.");

            server = new ServerSocket(1234);

            boolean running = true;

            while(running) {

                System.out.println("Client connected.");

                DataInputStream reader = new DataInputStream(client.getInputStream());
                DataOutputStream writer = new DataOutputStream(client.getOutputStream());
                ObjectInputStream   in =   new ObjectInputStream (client.getInputStream());
                ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
                // send 2 dimensional arrays to client


                // setups and sends arrays
                writer.writeUTF("Welcome to a Memory Game!");
                setup();
                os.writeObject(cards);
                os.writeObject(upDown);
            }


        }catch(IOException IOex){
            System.out.println("Server Error.  Connection Terminated.");

        }
    }

    public ServerSocket getSocket() {
        return server;
    }

    public static void setup() {
        for (int i = 0; i < 4; i++) {
            for (int a = 0; a < 4; a++) {
                upDown[i][a]=false;
            }
        }
        cards = randomizer(); //Shuffle cards
    }


    public static int[][] randomizer() {
        int num[] = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
        int cards[][] = new int[4][4];
        Random random = new Random();
        int temp, t;
        for (int j = 0; j <= 20; j++) {
            for (int x = 0; x < 16; x++) { //Randomize the card order
                t = random.nextInt(1000) % 15;
                temp = num[x];
                num[x] = num[t];
                num[t] = temp;

            }
            t = 0;
            for (int r = 0; r < 4; r++) // Cards receive Numbers
            {
                for (int s = 0; s < 4; s++) {
                    cards[r][s] = num[t];
                    t = t + 1;
                }
            }

        }
        return cards;
    }

    public static int[][] shuf() {
        int start[] = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
        int cards[][] = new int[4][4];
        Random ran = new Random();
        int tmp, i;
        for (int s = 0; s <= 20; s++) {
            for (int x = 0; x < 16; x++) //randomize the card placements
            {
                i = ran.nextInt(100000) % 15;
                tmp = start[x];
                start[x] = start[i];
                start[i] = tmp;
            }
        }
        i = 0;

        for (int r = 0; r < 4; r++) // put values in cards here
        {
            for (int c = 0; c < 4; c++) {
                cards[r][c] = start[i];
                i = i + 1;
            }
        }
        return cards;

    }







}