

import java.util.Random;


class PortAllocator {
    private final static int LOWER_BOUND = 49152;
    private final static int UPPER_BOUND = 65535;

    private final static Random RND = new Random(System.currentTimeMillis());

    static int[] allocatePorts(final int numPorts) {
        int[] ports = new int[numPorts];
        for (int i = 0; i < numPorts; i++) {
            int port = -1;
            while (port == -1) {
                port = RND.nextInt(UPPER_BOUND - LOWER_BOUND + 1) + LOWER_BOUND;
                for (int j = 0; j < i; j++) {
                    if (ports[j] == port) {
                        port = -1;
                        break;
                    }
                }
            }
            ports[i] = port;
        }
        return ports;
    }
}
