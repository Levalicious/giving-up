package network.threads;

import network.ReceivePool;

import java.net.DatagramSocket;

public class Listener extends Thread {
    private DatagramSocket socket;

    private ReceivePool received;

    private boolean running;

    public Listener(DatagramSocket socket) {
        this.socket = socket;
        this.received = ReceivePool.getInstance();
    }

    @Override
    public void run() {
        running = true;

        while(running) {

        }
    }


}
