import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.jar.*;
import java.util.zip.*;

public class TestExtra {

    static final int JAR_MAGIC = 0xcafe;

    static final int TEST_HEADER = 0xbabe;

    static final Charset ascii = Charset.forName("ASCII");

    static final byte[][] extra = new byte[][] { ascii.encode("hello, world").array(), ascii.encode("foo bar").array() };

    int count = 1;

    ByteArrayOutputStream baos;

    ZipOutputStream zos;

    public static void realMain(String[] args) throws Throwable {
        new TestExtra().testHeaderPlusData();
        new TestJarExtra().testHeaderPlusData();
        new TestJarExtra().testHeaderOnly();
        new TestJarExtra().testClientJarMagic();
    }

    TestExtra() {
        try {
            baos = new ByteArrayOutputStream();
            zos = getOutputStream(baos);
        } catch (Throwable t) {
            unexpected(t);
        }
    }

    void testHeaderPlusData() throws IOException {
        for (byte[] b : extra) {
            ZipEntry ze = getEntry();
            byte[] data = new byte[b.length + 4];
            set16(data, 0, TEST_HEADER);
            set16(data, 2, b.length);
            for (int i = 0; i < b.length; i++) {
                data[i + 4] = b[i];
            }
            ze.setExtra(data);
            zos.putNextEntry(ze);
        }
        zos.close();
        ZipInputStream zis = getInputStream();
        ZipEntry ze = zis.getNextEntry();
        checkEntry(ze, 0, extra[0].length);
        ze = zis.getNextEntry();
        checkEntry(ze, 1, extra[1].length);
    }

    void testHeaderOnly() throws IOException {
        ZipEntry ze = getEntry();
        byte[] data = new byte[4];
        set16(data, 0, TEST_HEADER);
        set16(data, 2, 0);
        ze.setExtra(data);
        zos.putNextEntry(ze);
        zos.close();
        ZipInputStream zis = getInputStream();
        ze = zis.getNextEntry();
        checkExtra(data, ze.getExtra());
        checkEntry(ze, 0, 0);
    }

    void testClientJarMagic() throws IOException {
        ZipEntry ze = getEntry();
        byte[] data = new byte[8];
        set16(data, 0, TEST_HEADER);
        set16(data, 2, 0);
        set16(data, 4, JAR_MAGIC);
        set16(data, 6, 0);
        ze.setExtra(data);
        zos.putNextEntry(ze);
        zos.close();
        ZipInputStream zis = getInputStream();
        ze = zis.getNextEntry();
        byte[] e = ze.getExtra();
        checkExtra(data, ze.getExtra());
        checkEntry(ze, 0, 0);
    }

    static void checkExtra(byte[] expected, byte[] extra) {
        if (expected == null)
            return;
        int off = 0;
        int len = expected.length;
        while (off + 4 < len) {
            int tag = get16(expected, off);
            int sz = get16(expected, off + 2);
            int off0 = 0;
            int len0 = extra.length;
            boolean matched = false;
            while (off0 + 4 < len0) {
                int tag0 = get16(extra, off0);
                int sz0 = get16(extra, off0 + 2);
                if (tag == tag0 && sz == sz0) {
                    matched = true;
                    for (int i = 0; i < sz; i++) {
                        if (expected[off + i] != extra[off0 + i])
                            matched = false;
                    }
                    break;
                }
                off0 += (4 + sz0);
            }
            if (!matched) {
                fail("Expected extra data [tag=" + tag + "sz=" + sz + "] check failed");
            }
            off += (4 + sz);
        }
    }

    void checkEntry(ZipEntry ze, int count, int dataLength) {
        byte[] extraData = ze.getExtra();
        byte[] data = getField(TEST_HEADER, extraData);
        if (!check(data != null, "unexpected null data for TEST_HEADER")) {
            return;
        }
        if (dataLength == 0) {
            check(data.length == 0, "unexpected non-zero data length for TEST_HEADER");
        } else {
            check(Arrays.equals(extra[count], data), "failed to get entry " + ze.getName() + ", expected " + new String(extra[count]) + ", got '" + new String(data) + "'");
        }
    }

    static byte[] getField(int descriptor, byte[] data) {
        byte[] rc = null;
        try {
            int i = 0;
            while (i < data.length) {
                if (get16(data, i) == descriptor) {
                    int length = get16(data, i + 2);
                    rc = new byte[length];
                    for (int j = 0; j < length; j++) {
                        rc[j] = data[i + 4 + j];
                    }
                    return rc;
                }
                i += get16(data, i + 2) + 4;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return rc;
    }

    ZipInputStream getInputStream() {
        return new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));
    }

    ZipOutputStream getOutputStream(ByteArrayOutputStream baos) throws IOException {
        return new ZipOutputStream(baos);
    }

    ZipInputStream getInputStream(ByteArrayInputStream bais) throws IOException {
        return new ZipInputStream(bais);
    }

    ZipEntry getEntry() {
        return new ZipEntry("zip" + count++ + ".txt");
    }

    private static int get16(byte[] b, int off) {
        return (b[off] & 0xff) | ((b[off + 1] & 0xff) << 8);
    }

    private static void set16(byte[] b, int off, int value) {
        b[off + 0] = (byte) value;
        b[off + 1] = (byte) (value >> 8);
    }

    static class TestJarExtra extends TestExtra {

        ZipOutputStream getOutputStream(ByteArrayOutputStream baos) throws IOException {
            return new JarOutputStream(baos);
        }

        ZipInputStream getInputStream(ByteArrayInputStream bais) throws IOException {
            return new JarInputStream(bais);
        }

        ZipEntry getEntry() {
            return new ZipEntry("jar" + count++ + ".txt");
        }

        void checkEntry(ZipEntry ze, int count, int dataLength) {
            if (count == 0) {
                byte[] extraData = ze.getExtra();
                byte[] data = getField(JAR_MAGIC, extraData);
                if (!check(data != null, "unexpected null data for JAR_MAGIC")) {
                    check(data.length != 0, "unexpected non-zero data length for JAR_MAGIC");
                }
            }
            super.checkEntry(ze, count, dataLength);
        }
    }

    static volatile int passed = 0, failed = 0;

    static void pass() {
        passed++;
    }

    static void fail() {
        failed++;
        Thread.dumpStack();
    }

    static void fail(String msg) {
        System.out.println(msg);
        fail();
    }

    static void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    static void check(boolean cond) {
        if (cond)
            pass();
        else
            fail();
    }

    static boolean check(boolean cond, String msg) {
        if (cond)
            pass();
        else
            fail(msg);
        return cond;
    }

    static void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y))
            pass();
        else
            fail(x + " not equal to " + y);
    }

    public static void main(String[] args) throws Throwable {
        try {
            realMain(args);
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.println("\nPassed = " + passed + " failed = " + failed);
        if (failed > 0)
            throw new Error("Some tests failed");
    }
}
