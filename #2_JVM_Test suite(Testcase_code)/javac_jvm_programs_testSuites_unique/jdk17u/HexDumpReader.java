import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HexDumpReader {

    public static InputStream getStreamFromHexDump(String fileName) {
        return getStreamFromHexDump(new File(System.getProperty("test.src", "."), fileName));
    }

    public static InputStream getStreamFromHexDump(File hexFile) {
        ByteArrayBuilder bab = new ByteArrayBuilder();
        int lineNo = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(hexFile), "us-ascii"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                int x = line.indexOf('#');
                if (x == 0) {
                    continue;
                }
                if (x > 0) {
                    line = line.substring(0, x).trim();
                }
                int len = line.length();
                for (int i = 0; i < len; i += 2) {
                    bab.put((byte) Integer.parseInt(line, i, i + 2, 16));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(hexFile.getName() + ":error:" + lineNo + ": " + e, e);
        }
        return new ByteArrayInputStream(bab.toArray());
    }

    private static class ByteArrayBuilder {

        private static final int BUFFER_SIZE = 4096;

        private int size;

        private List<byte[]> bytes;

        private byte[] current;

        private int offset;

        ByteArrayBuilder() {
            bytes = new ArrayList<>();
            current = new byte[BUFFER_SIZE];
        }

        void put(byte b) {
            if (offset == BUFFER_SIZE) {
                bytes.add(current);
                current = new byte[BUFFER_SIZE];
                offset = 0;
            }
            current[offset++] = b;
            size++;
        }

        byte[] toArray() {
            byte[] buf = new byte[size];
            int ptr = 0;
            for (byte[] ba : bytes) {
                System.arraycopy(ba, 0, buf, ptr, ba.length);
                ptr += ba.length;
            }
            System.arraycopy(current, 0, buf, ptr, offset);
            assert ptr + offset == size;
            return buf;
        }
    }
}
