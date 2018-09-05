package network.messageTypes;

import static network.NetUtils.getData;

public class Data {
    private int messageType;

    private byte[] messageContents;

    public Data(BigMessage m) {
        this.messageType = m.getMessageType();
        this.messageContents = m.getData();
    }
}
