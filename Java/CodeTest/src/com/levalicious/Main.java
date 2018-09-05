package com.levalicious;

public class Main {

    public static void main(String[] args) throws Exception {
        Server x = new Server();
        x.run();
        Thread.sleep(4000);
        System.out.println("Client starting.");
        new Client(x.getSocket()).run();

    }
}
