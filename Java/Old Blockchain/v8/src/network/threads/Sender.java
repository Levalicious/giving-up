package network.threads;

import network.ReceivePool;
import network.SendPool;

import java.net.DatagramSocket;

public class Sender extends Thread {
    private DatagramSocket socket;

    private SendPool toSend;

    private boolean running;

    public Sender(DatagramSocket socket) {
        this.socket = socket;
        this.toSend = SendPool.getInstance();
    }

    @Override
    public void run() {
        running = true;

        while(running) {

        }
    }
}
