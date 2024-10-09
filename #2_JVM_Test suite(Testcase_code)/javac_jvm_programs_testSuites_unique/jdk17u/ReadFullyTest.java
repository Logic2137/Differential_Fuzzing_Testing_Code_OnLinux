import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteOrder;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

public class ReadFullyTest {

    static final ByteOrder bigEndian = ByteOrder.BIG_ENDIAN;

    static final ByteOrder littleEndian = ByteOrder.LITTLE_ENDIAN;

    private static void expect(long e, long g) {
        if (e != g) {
            throw new RuntimeException("Expected " + e + ", got " + g);
        }
    }

    public static void main(String[] args) {
        try {
            byte[] b = { 
            (byte) 0x11, (byte) 0x22, 
            (byte) 0x44, (byte) 0x99, 
            (byte) 0xAA, (byte) 0x33, (byte) 0xBB, (byte) 0xCC };
            InputStream in = new ByteArrayInputStream(b);
            ImageInputStream iin = new MemoryCacheImageInputStream(in);
            short[] s = new short[b.length / 2];
            char[] c = new char[b.length / 2];
            int[] i = new int[b.length / 4];
            long[] l = new long[b.length / 8];
            float[] f = new float[b.length / 4];
            double[] d = new double[b.length / 8];
            iin.seek(0L);
            iin.setByteOrder(bigEndian);
            iin.readFully(s, 0, s.length);
            expect(s[0] & 0xffff, 0x1122);
            expect(s[1] & 0xffff, 0x4499);
            expect(s[2] & 0xffff, 0xAA33);
            expect(s[3] & 0xffff, 0xBBCC);
            iin.seek(0L);
            iin.setByteOrder(littleEndian);
            iin.readFully(s, 0, s.length);
            expect(s[0] & 0xffff, 0x2211);
            expect(s[1] & 0xffff, 0x9944);
            expect(s[2] & 0xffff, 0x33AA);
            expect(s[3] & 0xffff, 0xCCBB);
            iin.seek(0L);
            iin.setByteOrder(bigEndian);
            iin.readFully(c, 0, c.length);
            expect(c[0], 0x1122);
            expect(c[1], 0x4499);
            expect(c[2], 0xAA33);
            expect(c[3], 0xBBCC);
            iin.seek(0L);
            iin.setByteOrder(littleEndian);
            iin.readFully(c, 0, c.length);
            expect(c[0], 0x2211);
            expect(c[1], 0x9944);
            expect(c[2], 0x33AA);
            expect(c[3], 0xCCBB);
            iin.seek(0L);
            iin.setByteOrder(bigEndian);
            iin.readFully(i, 0, i.length);
            expect(i[0] & 0xffffffff, 0x11224499);
            expect(i[1] & 0xffffffff, 0xAA33BBCC);
            iin.seek(0L);
            iin.setByteOrder(littleEndian);
            iin.readFully(i, 0, i.length);
            expect(i[0] & 0xffffffff, 0x99442211);
            expect(i[1] & 0xffffffff, 0xCCBB33AA);
            iin.seek(0L);
            iin.setByteOrder(bigEndian);
            iin.readFully(f, 0, f.length);
            expect(Float.floatToIntBits(f[0]) & 0xffffffff, 0x11224499);
            expect(Float.floatToIntBits(f[1]) & 0xffffffff, 0xAA33BBCC);
            iin.seek(0L);
            iin.setByteOrder(littleEndian);
            iin.readFully(f, 0, f.length);
            expect(Float.floatToIntBits(f[0]) & 0xffffffff, 0x99442211);
            expect(Float.floatToIntBits(f[1]) & 0xffffffff, 0xCCBB33AA);
            iin.seek(0L);
            iin.setByteOrder(bigEndian);
            iin.readFully(l, 0, l.length);
            expect(l[0], 0x11224499AA33BBCCL);
            iin.seek(0L);
            iin.setByteOrder(littleEndian);
            iin.readFully(l, 0, l.length);
            expect(l[0], 0xCCBB33AA99442211L);
            iin.seek(0L);
            iin.setByteOrder(bigEndian);
            iin.readFully(d, 0, d.length);
            expect(Double.doubleToLongBits(d[0]), 0x11224499AA33BBCCL);
            iin.seek(0L);
            iin.setByteOrder(littleEndian);
            iin.readFully(d, 0, d.length);
            expect(Double.doubleToLongBits(d[0]), 0xCCBB33AA99442211L);
        } catch (Exception ex) {
            throw new RuntimeException("Got exception " + ex);
        }
    }
}
