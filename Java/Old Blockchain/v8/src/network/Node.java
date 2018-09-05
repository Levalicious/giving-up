package network;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;

import network.messageTypes.MessageProtos.*;

public class Node {
    private DatagramSocket socket;

    private SendPool toSend;

    private ReceivePool received;

    private byte[] self;

    /*
    CURRENT THOUGHTS:
    Message is received by listener. This interface automagically grabs any messages
    in the listener's stack, parses them as a message, and tosses them into the message
    processor.

    From there, it grabs any available messages that are headed for the relay from the
    message processor and routes them into the sender

    It also grabs any acknowledgers and feeds them into the relay

    The relay contains 2 processes.

    The first is the relay process itself, and the second is an Acknowledger.

    The relay maintains two stacks. A "submission" stack and an actual toSend stack
    When the relay receives a message it gets tossed into the submission stack

    From there, the relay class contains a processSubmission method

    What that method does is take a submission from the submission stack, submit it to
    the acknowledger as a "to be acknowledged" message, and also to the toSend stack

    As soon as it processes submissions, it checks the Acknowledger for any pending
    re-sends and adds those directly to the toSend stack, not the submission stack.

    After that, it grabs every message in the toSend stack and sends them to their
    destination.

    The Acknowledger Class:
    The acknowledger is periodic.
    Every two seconds, anything in the acknowledgements list is used to trim the list
    of things pending acknowledgement.
    Then, every item still on the pending acknowledgement list gets an iterated
    relay # and gets copied to a stack for the relay to grab from.


    Format:
    Hex ID - Title (Datafield, Response)

    Message Type Identifiers:
    0x00 - ????
    0x01 - Join (N/A, 0x03 or 0x04)
    0x02 - Leave (N/A, N/A)
    0x03 - Yes (N/A, N/A)
    0x04 - No (N/A, N/A)
    0x05 - Ping (N/A, 0x06)
    0x06 - Pong (N/A, N/A)
    0x07 - Acknowledge (MIC, N/A)
    0x08 - Transaction (Transaction, 0x07)
    0x09 - Block (Block, 0x07)
    0x0A - Peer (Peer, 0x07)
    0x0B - PeerList (PeerList, 0x07)
    0x0C - Message (Message, 0x07)
    0x0D - Transaction Request (N/A, 0x07)
    0x0E - Block Request (N/A, 0x09)
    0x0F - Peer Request (N/A, 0x0A or 0x0B)


    Message Identification Code (MIC):
    Made by concatenating the message received, the index of that message, and the
    address of the sender of that message
     */

    public Node() {

    }

    public byte[] getId() {
        return self;
    }

    public void schedulePing(Peer p) {

    }
}
