

import java.io.IOException;
import java.net.*;
import java.util.Set;
import static java.lang.System.out;






public class SupportedOptionsSet {

    public static void main(String[] args) throws IOException {
        if (args[0].equals("first"))
            first();
        else if (args[0].equals("second"))
            second();
    }

    static void first() throws IOException {
        try (Socket s = new Socket();
             ServerSocket ss = new ServerSocket();
             DatagramSocket ds = new DatagramSocket();
             MulticastSocket ms = new MulticastSocket()) {

            Set<?> first = s.supportedOptions();
            Set<?> second = ss.supportedOptions();
            assertNotEqual(first, second,
                 "Socket and ServerSocket should have different options.");

            first = ds.supportedOptions();
            second = ms.supportedOptions();
            assertNotEqual(first, second,
                "DatagramSocket and MulticastSocket should have different options.");
        }
    }

    
    static void second() throws IOException {
        try (ServerSocket ss = new ServerSocket();
             Socket s = new Socket();
             DatagramSocket ds = new DatagramSocket();
             MulticastSocket ms = new MulticastSocket()) {

            Set<?> first = ss.supportedOptions();
            Set<?> second = s.supportedOptions();
            assertNotEqual(first, second,
                "ServerSocket and Socket should have different options.");

            first = ms.supportedOptions();
            second = ds.supportedOptions();
            assertNotEqual(first, second,
                "MulticastSocket and DatagramSocket should have different options.");

        }
    }

    static void assertNotEqual(Set<?> s1, Set<?> s2, String message) {
        if (s1.equals(s2)) {
            out.println("s1: " + s1);
            out.println("s2: " + s2);
            throw new RuntimeException(message);
        }
    }
}
