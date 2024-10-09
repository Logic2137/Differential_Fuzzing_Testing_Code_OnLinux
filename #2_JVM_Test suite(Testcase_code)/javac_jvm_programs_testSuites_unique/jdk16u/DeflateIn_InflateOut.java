



import java.io.*;
import java.util.*;
import java.util.zip.*;

public class DeflateIn_InflateOut {
    private static byte[] data = new byte[1024 * 1024];

    private static ByteArrayInputStream bais;
    private static DeflaterInputStream dis;

    private static ByteArrayOutputStream baos;
    private static InflaterOutputStream ios;

    private static Inflater reset(byte[] dict) {
        bais = new ByteArrayInputStream(data);
        if (dict == null) {
            dis = new DeflaterInputStream(bais);
        } else {
            Deflater def = new Deflater();
            def.setDictionary(dict);
            dis = new DeflaterInputStream(bais, def);
        }

        baos = new ByteArrayOutputStream();
        if (dict == null) {
            ios = new InflaterOutputStream(baos);
            return null;
        } else {
            Inflater inf = new Inflater();
            ios = new InflaterOutputStream(baos, inf);
            return inf;
        }
    }

    private static void reset() {
        reset(null);
    }

    
    private static void ArrayReadWrite() throws Throwable {
        byte[] buf = new byte[512];

        reset();
        check(dis.available() == 1);
        for (;;) {
            int len = dis.read(buf, 0, buf.length);
            if (len < 0) {
                break;
            } else {
                ios.write(buf, 0, len);
            }
        }
        check(dis.available() == 0);
        ios.close();
        check(Arrays.equals(data, baos.toByteArray()));
    }

    
    private static void ArrayReadByteWrite() throws Throwable {
        byte[] buf = new byte[512];

        reset();
        for (;;) {
            int len = dis.read(buf, 0, buf.length);
            if (len <= 0) {
                break;
            } else {
                for (int i = 0; i < len; i++) {
                    byte x = (byte) (buf[i] & 0xff);
                    ios.write(x);
                }
            }
        }
        check(dis.available() == 0);
        ios.close();
        check(Arrays.equals(data, baos.toByteArray()));
    }

    
    private static void ByteReadArrayWrite() throws Throwable {
        byte[] buf = new byte[8192];
        int off = 0;

        reset();
        int datum = dis.read();
        while (datum != -1) {
            if (off == 8192) {
                ios.write(buf, 0, off);
                off = 0;
            }
            buf[off++] = (byte) (datum & 0xff);
            datum = dis.read();
        }
        if (off > 0) {
            ios.write(buf, 0, off);
        }
        ios.close();
        check(Arrays.equals(data, baos.toByteArray()));
    }

    
    private static void ByteReadByteWrite() throws Throwable {
        byte[] buf = new byte[512];
        boolean reachEOF = false;

        reset();
        while (dis.available() == 1) {
            int datum = dis.read();
            if (datum == -1) {
                reachEOF = true;
            } else {
                if (datum < 0 || datum > 255) {
                    fail("datum out of range: " + datum);
                }
                ios.write(datum);
            }
        }
        dis.close();
        ios.close();
        check(data[0] == baos.toByteArray()[0]);
    }

    
    private static void SkipBytes() throws Throwable {
        byte[] buf = new byte[512];
        int numReadable = 0;

        
        reset();
        check(dis.available() == 1);
        for (;;) {
            int count = dis.read(buf, 0, buf.length);
            if (count < 0) {
                break;
            } else {
                numReadable += count;
            }
        }
        check(dis.available() == 0);

        
        reset();
        int numNotSkipped = 0;
        int numSkipBytes = 2053; 
        check(dis.skip(numSkipBytes) == numSkipBytes);
        for (int i = 0; ; i++) {
            int count = dis.read(buf, 0, buf.length);
            if (count < 0) {
                break;
            } else {
                numNotSkipped += count;
            }
        }
        check(numNotSkipped + numSkipBytes == numReadable);

        
        reset();
        numNotSkipped = 0;
        numSkipBytes = 8887; 
        for (int i = 0; ; i++) {
            if (i == 13) { 
                check(dis.skip(numSkipBytes) == numSkipBytes);
            } else {
                int count = dis.read(buf, 0, buf.length);
                if (count < 0) {
                    break;
                } else {
                    numNotSkipped += count;
                }
            }
        }
        check(numNotSkipped + numSkipBytes == numReadable);

        
        reset();
        numNotSkipped = 0;
        numSkipBytes = 6449; 
        for (int i = 0; ; i++) {
            if (numNotSkipped + numSkipBytes > numReadable) {
                numSkipBytes = numReadable - numNotSkipped;
                check(dis.skip(numSkipBytes) == numSkipBytes);
                check(dis.read(buf, 0, buf.length) == -1);
                check(dis.available() == 0);
            } else {
                int count = dis.read(buf, 0, buf.length);
                if (count < 0) {
                    break;
                } else {
                    numNotSkipped += count;
                }
            }
        }
        check(numNotSkipped + numSkipBytes == numReadable);
    }

    
    private static void NeedsDictionary() throws Throwable {
        byte[] dict = {1, 2, 3, 4};
        Adler32 adler32 = new Adler32();
        adler32.update(dict);
        long checksum = adler32.getValue();
        byte[] buf = new byte[512];

        Inflater inf = reset(dict);
        check(dis.available() == 1);
        boolean dictSet = false;
        for (;;) {
            int len = dis.read(buf, 0, buf.length);
            if (len < 0) {
                break;
            } else {
                try {
                    ios.write(buf, 0, len);
                    if (dictSet == false) {
                        check(false, "Must throw ZipException without dictionary");
                        return;
                    }
                } catch (ZipException ze) {
                    check(dictSet == false, "Dictonary must be set only once");
                    check(checksum == inf.getAdler(), "Incorrect dictionary");
                    inf.setDictionary(dict);
                    
                    
                    
                    ios.flush();
                    dictSet = true;
                }
            }
        }
        check(dis.available() == 0);
        ios.close();
        check(Arrays.equals(data, baos.toByteArray()));
    }

    public static void realMain(String[] args) throws Throwable {
        new Random(new Date().getTime()).nextBytes(data);

        ArrayReadWrite();

        ArrayReadByteWrite();

        ByteReadArrayWrite();

        ByteReadByteWrite();

        SkipBytes();

        NeedsDictionary();
    }

    
    static volatile int passed = 0, failed = 0;
    static void pass() {passed++;}
    static void fail() { fail(null); }
    static void fail(String msg) {
        failed++;
        if (msg != null) {
            System.err.println(msg);
        }
        Thread.dumpStack();
    }
    static void unexpected(Throwable t) {failed++; t.printStackTrace();}
    static void check(boolean cond) {if (cond) pass(); else fail();}
    static void check(boolean cond, String msg) {if (cond) pass(); else fail(msg);}
    static void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y)) pass();
        else fail(x + " not equal to " + y);}
    public static void main(String[] args) throws Throwable {
        try {realMain(args);} catch (Throwable t) {unexpected(t);}
        System.out.println("\nPassed = " + passed + " failed = " + failed);
        if (failed > 0) throw new AssertionError("Some tests failed");}
}
