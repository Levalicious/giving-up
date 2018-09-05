package core;

import java.util.concurrent.ConcurrentHashMap;

public class NetworkState {
    private ConcurrentHashMap<byte[], AccountState> state;

    public NetworkState() {
        this.state = new ConcurrentHashMap<>();
    }

    public byte[] getStateHash() {
        return null;
    }
}
