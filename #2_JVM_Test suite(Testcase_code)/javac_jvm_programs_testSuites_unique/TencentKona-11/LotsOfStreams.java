



import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Enumeration;

public class LotsOfStreams {

    static final int MAX_SUBSTREAMS = 32000;

    public static void main(String[] argv) throws Exception {
        try (InputStream stream =
                new SequenceInputStream(new LOSEnumeration())) {
            stream.read();
        }
        try (InputStream stream =
                new SequenceInputStream(new LOSEnumeration())) {
            byte[] b = new byte[1];
            stream.read(b, 0, 1);
        }
    }

    static class LOSEnumeration
            implements Enumeration<InputStream> {

        private static InputStream inputStream =
                new ByteArrayInputStream(new byte[0]);
        private int left = MAX_SUBSTREAMS;

        public boolean hasMoreElements() {
            return (left > 0);
        }
        public InputStream nextElement() {
            left--;
            return inputStream;
        }
    }
}
