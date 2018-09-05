package org.levk.UDP2Pv2.network;

import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;

public class Node {
    public static boolean isIPv6 = false;

    public Node() throws SocketException {
        checkIPv6();
    }

    private void checkIPv6() throws SocketException {
        final Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();

        while (e.hasMoreElements()) {
            final Iterator<InterfaceAddress> e2 = e.nextElement().getInterfaceAddresses().iterator();

            while (e2.hasNext()) {
                final InetAddress ip = e2.next().getAddress();

                if (ip.isLoopbackAddress() || ip instanceof Inet4Address) {
                    continue;
                } else {
                    isIPv6 = true;
                }
            }
        }
    }
}
