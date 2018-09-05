package network.buckets;

import network.Peer;

import java.util.ArrayList;

public class Bucket {
    private Peer[] peers;
    private int k;
    private int peerCount;

    Bucket(int k) {
        this.k = k;
        this.peers = new Peer[k];
        this.peerCount = 0;
    }

    public boolean add(Peer p) {
        int toBeReplaced = checkPeers();
        if(!(toBeReplaced < 0)) {
            peers[toBeReplaced] = p;
            peerCount++;
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(Peer p) {
        for(int i = 0; i < k; i++) {
            if(peers[i] == p) {
                peers[i] = null;
                peerCount--;
                return true;
            }
        }

        return false;
    }

    public int getPeerCount() {
        return peerCount;
    }

    public void trimBucket() {
        for(int i = 0; i < k; i++) {
            if(peers[i].isOld()) {
                if(peers[i].toTerminate()) {
                    peers[i] = null;
                    peerCount--;
                }

                /* TODO: Schedule a ping to this peer */
            }
        }
    }

    public ArrayList<Peer> getPeers() {
        ArrayList<Peer> temp = new ArrayList<>();

        for(int i = 0; i < k; i++) {
            if(peers[i] != null) {
                temp.add(peers[i]);
            }
        }

        return temp;
    }

    private int checkPeers() {
        for(int i = 0; i < k; i++) {
            if(peers[i] == null) {
                return i;
            }

            if(peers[i].isOld()) {
                if(peers[i].toTerminate()) {
                    return i;
                }

                /* TODO: Schedule a ping to this peer */
            }
        }

        return -1;
    }
}
