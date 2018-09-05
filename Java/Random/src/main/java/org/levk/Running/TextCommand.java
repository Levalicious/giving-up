package org.levk.Running;

import org.bouncycastle.util.encoders.Hex;
import org.levk.p2pnet.network.commands.NetworkCommand;
import org.levk.p2pnet.network.messages.Message;
import org.levk.p2pnet.util.Tuple;

public class TextCommand extends NetworkCommand {
    @Override
    public Tuple<Integer, byte[]> handle (Message in) {
        try {
            System.out.println(Hex.toHexString(in.getSender()) + ": " + new String(in.getPayload(), "UTF-8"));
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
