import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

public class SMFParserBreak {

    public static void main(String[] args) throws Exception {
        InputStream is = new ByteArrayInputStream(midifile);
        is = new ChunkInputStream(is, 32);
        Sequence sequence = MidiSystem.getSequence(is);
        long duration = sequence.getMicrosecondLength() / 10000;
        System.out.println("Duration: " + duration + " deciseconds ");
        System.out.println("Test passed");
    }

    static byte[] midifile = { 77, 84, 104, 100, 0, 0, 0, 6, 0, 1, 0, 3, -30, 120, 77, 84, 114, 107, 0, 0, 0, 123, 0, -112, 30, 100, -113, 49, -128, 50, 100, -114, 69, -112, 31, 100, -114, 33, -128, 51, 100, -114, 55, -112, 32, 100, -114, 120, -128, 52, 100, -114, 40, -112, 33, 100, -114, 26, -128, 53, 100, -114, 26, -112, 34, 100, -114, 76, -128, 54, 100, -114, 12, -112, 35, 100, -114, 91, -128, 55, 100, -114, 69, -112, 36, 100, -114, 33, -128, 56, 100, -114, 55, -112, 37, 100, -114, 84, -128, 57, 100, -114, 40, -112, 38, 100, -114, 26, -128, 58, 100, -114, 26, -112, 39, 100, -113, 24, -128, 59, 100, -113, 60, -112, 40, 100, -113, 110, -128, 60, 100, -113, 96, -112, 41, 100, -113, 39, -128, 61, 100, 0, -1, 47, 0, 77, 84, 114, 107, 0, 0, 0, 4, 0, -1, 47, 0, 77, 84, 114, 107, 0, 0, 0, 4, 0, -1, 47, 0 };
}

class ChunkInputStream extends FilterInputStream {

    int chunkSize;

    int p = 0;

    public ChunkInputStream(InputStream is, int chunkSize) {
        super(is);
        this.chunkSize = chunkSize;
    }

    public int read() throws IOException {
        int ret = super.read();
        if (ret >= 0) {
            p++;
        }
        return ret;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if ((p / chunkSize) < ((p + len) / chunkSize)) {
            len -= ((p + len) % chunkSize);
        }
        int ret = super.read(b, off, len);
        if (ret >= 0) {
            p += ret;
        }
        return ret;
    }
}
