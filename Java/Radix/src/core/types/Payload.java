package core.types;

import static util.ByteUtil.concat;

public class Payload {
    private byte[] data;
    private Participants participants;

    Payload(byte[] data, byte[] sender, byte[] receiver) {
        this.data = data;
        participants = new Participants(sender, receiver);
    }

    public byte[] getBytes() {
        byte[] temp = new byte[2];
        temp[0] = (byte)0x04;
        temp[1] = (byte)data.length;
        return concat(temp, data, participants.getBytes());
    }

    public class Participants {
        private byte[] sender;
        private byte[] receiver;

        Participants(byte[] sender, byte[] receiver) {
            this.sender = sender;
            this.receiver = receiver;
        }

        public byte[] getSender() {
            return sender;
        }

        public byte[] getReceiver() {
            return receiver;
        }

        public byte[] getBytes() {
            byte[] sendTemp = new byte[1];
            byte[] receiveTemp = new byte[1];
            sendTemp[0] = (byte)0x05;
            receiveTemp[0] = (byte)0x05;
            return concat(sendTemp,sender,receiveTemp,receiver);
        }
    }
}
