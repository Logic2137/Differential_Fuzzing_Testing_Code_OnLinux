



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.ImageInputStream;

public class ReadBitsTest {
    public static void main(String[] args) throws IOException {
        byte[] buffer = new byte[] {(byte)169, (byte)85}; 
        InputStream ins = new ByteArrayInputStream(buffer);
        ImageInputStream in = new FileCacheImageInputStream(ins,null);

        if (in.getBitOffset() != 0) {
            throw new RuntimeException("Initial bit offset != 0!");
        }

        int bit0 = in.readBit(); 
        if (bit0 != 1) {
            throw new RuntimeException("First bit != 1");
        }
        if (in.getBitOffset() != 1) {
            throw new RuntimeException("Second bit offset != 1");
        }

        long bits1 = in.readBits(5); 
        if (bits1 != 10) {
            throw new RuntimeException("Bits 1-5 != 10 (= " + bits1 + ")");
        }
        if (in.getBitOffset() != 6) {
            throw new RuntimeException("Third bit offset != 6");
        }

        int bit1 = in.readBit(); 
        if (bit1 != 0) {
            throw new RuntimeException("Bit 6 != 0");
        }
        if (in.getBitOffset() != 7) {
            throw new RuntimeException("Third bit offset != 7");
        }

        long bits2 = in.readBits(8); 
        if (bits2 != 170) {
            throw new RuntimeException("Bits 7-14 != 170 (= " + bits2 + ")");
        }
        if (in.getBitOffset() != 7) {
            throw new RuntimeException("Fourth bit offset != 7");
        }

        int bit2 = in.readBit(); 
        if (bit2 != 1) {
            throw new RuntimeException("Bit 15 != 1");
        }
        if (in.getBitOffset() != 0) {
            throw new RuntimeException("Fifth bit offset != 0");
        }

        in.close();
    }
}
