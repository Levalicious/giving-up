package org.levk.UDP2Pv2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import org.bouncycastle.util.encoders.Hex;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.jgrapht.Graph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.levk.UDP2Pv2.network.peerStorage.NoSpaceException;
import org.levk.UDP2Pv2.network.peerStorage.Peer;
import org.levk.UDP2Pv2.network.peerStorage.PeerSet;
import org.levk.UDP2Pv2.util.datastore.FileDB;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

public class Start {
    static SecureRandom rand = new SecureRandom();

    static long putTime = 0;

    static long putcount = 0;

    public static void main(String[] args) throws Exception {
        byte[] key = new byte[32];
        byte[] val = new byte[32];

        FileDB db = new FileDB("tempstore");

        long start = 0;
        long end = 0;

        for (int i = 0; i < 1000000; i++) {
            rand.nextBytes(key);
            rand.nextBytes(val);


            start = System.nanoTime();
            db.put(key, val);
            end = System.nanoTime();

            putTime += (end - start);

            putcount++;

            System.out.print("\r" + putcount + "/1000000");
        }

        System.out.println();
        System.out.println("On average took " + (putTime / putcount) + " ns per insert.");
    }
}
