package network.buckets;

import network.Peer;

import java.util.ArrayList;

public class Buckets {
    private int k;
    private Bucket[] buckets = new Bucket[256];
    private Peer nodeAddress;

    /* Create new bucket set with xor distance calculated from peer p, and a k value */
    public Buckets(Peer p, int k) {
        this.nodeAddress = p;
        this.k = k;
    }

    /* Attempt to add a peer. If successful, return true. Else, return false. */
    public boolean add(Peer p) {
        if(buckets[p.calcDist(nodeAddress).indexOf('1')] == null) {
            buckets[p.calcDist(nodeAddress).indexOf('1')] = new Bucket(k);
        }

        return buckets[p.calcDist(nodeAddress).indexOf('1')].add(p);
    }

    /* Attempt to remove a peer. If peer is not in the list of peers, return false. */
    public boolean remove(Peer p) {
        if(buckets[p.calcDist(nodeAddress).indexOf('1')] == null) {
            buckets[p.calcDist(nodeAddress).indexOf('1')] = new Bucket(k);
        }

        return buckets[p.calcDist(nodeAddress).indexOf('1')].remove(p);
    }

    /* Grab list of all peers. Useful for broadcasting messages */
    public ArrayList<Peer> getPeers() {
        ArrayList<Peer> temp = new ArrayList<>();

        for(int i = 0; i < 256; i++) {
            if(buckets[i] != null) {
                temp.addAll(buckets[i].getPeers());
            }
        }

        return temp;
    }

    /* Grab list of peers closest to peer p. Useful for message routing & whisper */
    public ArrayList<Peer> getClosest(Peer p) {
        /* Find the bucket of peers closest to the target */
        int targetBucket = p.calcDist(nodeAddress).indexOf('1');

        /* If said bucket has peers in it, grab those peers and return them */
        if(buckets[targetBucket] != null && buckets[targetBucket].getPeerCount() > 0) {
            return buckets[targetBucket].getPeers();
        }

        /* If the target bucket did not have peers, attempt the next closest buckets. */
        for(int i = 1; i <= 128; i++) {
            /* Identify the indices of the two closest buckets */
            int high = targetBucket + i;
            int low = targetBucket - i;

            /* If the indices go out of the bounds of the array, set them at the boundary. */
            if(low < 0) low = 0;
            if(high > 255) high = 255;

            /* Establish a peer count for each bucket */
            int highCount = 0;
            int lowCount = 0;

            /* If the next closest bucket exists, grab the peercount from it */
            if(buckets[high] != null) {
                highCount = buckets[high].getPeerCount();
            }

            /* If the next farthest bucket exists, grab the peercount from it */
            if(buckets[low] != null) {
                lowCount = buckets[low].getPeerCount();
            }

            /* If at least one of the two buckets targeted has at least one peer */
            if(highCount > 0 || lowCount > 0) {
                /* Identify the bucket containing the most peers and return that bucket */
                if(highCount > lowCount) {
                    return buckets[high].getPeers();
                } else {
                    return buckets[low].getPeers();
                }
            }
        }

        return null;
    }
}
