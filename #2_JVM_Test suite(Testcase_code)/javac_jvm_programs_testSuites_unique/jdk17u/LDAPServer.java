
package test;

import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class LDAPServer {

    private final Map<Integer, List<byte[]>> cache = new HashMap<>();

    public LDAPServer(ServerSocket serverSocket, String filename) throws Exception {
        System.out.println("LDAPServer: Loading LDAP cache from: " + filename);
        loadCaptureFile(filename);
        System.out.println("LDAPServer: listening on port " + serverSocket.getLocalPort());
        try (Socket clientSocket = serverSocket.accept();
            OutputStream out = clientSocket.getOutputStream();
            InputStream in = clientSocket.getInputStream()) {
            byte[] inBuffer = new byte[8192];
            int count;
            while ((count = in.read(inBuffer)) > 0) {
                byte[] request = Arrays.copyOf(inBuffer, count);
                int[] ids = getIDs(request);
                int messageID = ids[0];
                String operation = getOperation(ids[1]);
                System.out.println("\nLDAPServer: received LDAP " + operation + "  [message ID " + messageID + "]");
                List<byte[]> encodings = cache.get(messageID);
                if (encodings == null || (!Arrays.equals(request, encodings.get(0)))) {
                    throw new Exception("LDAPServer: ERROR: received an LDAP " + operation + " (ID=" + messageID + ") not present in cache");
                }
                for (int i = 1; i < encodings.size(); i++) {
                    byte[] response = encodings.get(i);
                    out.write(response, 0, response.length);
                    ids = getIDs(response);
                    System.out.println("\nLDAPServer: Sent LDAP " + getOperation(ids[1]) + "  [message ID " + ids[0] + "]");
                }
            }
        } catch (IOException e) {
            System.out.println("LDAPServer: ERROR: " + e);
            throw e;
        }
        System.out.println("\n[LDAP server exited normally]");
    }

    private void loadCaptureFile(String filename) throws IOException {
        StringBuilder hexString = new StringBuilder();
        String pattern = "(....): (..) (..) (..) (..) (..) (..) (..) (..)   (..) (..) (..) (..) (..) (..) (..) (..).*";
        try (Scanner fileScanner = new Scanner(Paths.get(filename))) {
            while (fileScanner.hasNextLine()) {
                try (Scanner lineScanner = new Scanner(fileScanner.nextLine())) {
                    if (lineScanner.findInLine(pattern) == null) {
                        continue;
                    }
                    MatchResult result = lineScanner.match();
                    for (int i = 1; i <= result.groupCount(); i++) {
                        String digits = result.group(i);
                        if (digits.length() == 4) {
                            if (digits.equals("0000")) {
                                if (hexString.length() > 0) {
                                    addToCache(hexString.toString());
                                    hexString = new StringBuilder();
                                }
                            }
                            continue;
                        } else if (digits.equals("  ")) {
                            continue;
                        }
                        hexString.append(digits);
                    }
                }
            }
        }
        addToCache(hexString.toString());
    }

    private void addToCache(String hexString) throws IOException {
        byte[] encoding = parseHexBinary(hexString);
        int[] ids = getIDs(encoding);
        int messageID = ids[0];
        List<byte[]> encodings = cache.get(messageID);
        if (encodings == null) {
            encodings = new ArrayList<>();
        }
        System.out.println("    adding LDAP " + getOperation(ids[1]) + " with message ID " + messageID + " to the cache");
        encodings.add(encoding);
        cache.put(messageID, encodings);
    }

    private static int[] getIDs(byte[] encoding) throws IOException {
        if (encoding[0] != 0x30) {
            throw new IOException("Error: bad LDAP encoding in capture file: " + "expected ASN.1 SEQUENCE tag (0x30), encountered " + encoding[0]);
        }
        int index = 2;
        if ((encoding[1] & 0x80) == 0x80) {
            index += (encoding[1] & 0x0F);
        }
        if (encoding[index] != 0x02) {
            throw new IOException("Error: bad LDAP encoding in capture file: " + "expected ASN.1 INTEGER tag (0x02), encountered " + encoding[index]);
        }
        int length = encoding[index + 1];
        index += 2;
        int messageID = new BigInteger(1, Arrays.copyOfRange(encoding, index, index + length)).intValue();
        index += length;
        int operationID = encoding[index];
        return new int[] { messageID, operationID };
    }

    private static String getOperation(int operationID) {
        switch(operationID) {
            case 0x60:
                return "BindRequest";
            case 0x61:
                return "BindResponse";
            case 0x42:
                return "UnbindRequest";
            case 0x63:
                return "SearchRequest";
            case 0x64:
                return "SearchResultEntry";
            case 0x65:
                return "SearchResultDone";
            case 0x66:
                return "ModifyRequest";
            case 0x67:
                return "ModifyResponse";
            case 0x68:
                return "AddRequest";
            case 0x69:
                return "AddResponse";
            case 0x4A:
                return "DeleteRequest";
            case 0x6B:
                return "DeleteResponse";
            case 0x6C:
                return "ModifyDNRequest";
            case 0x6D:
                return "ModifyDNResponse";
            case 0x6E:
                return "CompareRequest";
            case 0x6F:
                return "CompareResponse";
            case 0x50:
                return "AbandonRequest";
            case 0x73:
                return "SearchResultReference";
            case 0x77:
                return "ExtendedRequest";
            case 0x78:
                return "ExtendedResponse";
            case 0x79:
                return "IntermediateResponse";
            default:
                return "Unknown";
        }
    }

    public static byte[] parseHexBinary(String s) {
        final int len = s.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + s);
        }
        byte[] out = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int h = hexToBin(s.charAt(i));
            int l = hexToBin(s.charAt(i + 1));
            if (h == -1 || l == -1) {
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + s);
            }
            out[i / 2] = (byte) (h * 16 + l);
        }
        return out;
    }

    private static int hexToBin(char ch) {
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        }
        if ('A' <= ch && ch <= 'F') {
            return ch - 'A' + 10;
        }
        if ('a' <= ch && ch <= 'f') {
            return ch - 'a' + 10;
        }
        return -1;
    }

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    public static String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }
}
