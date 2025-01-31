



package compiler.intrinsics.zip;

import java.nio.ByteBuffer;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;

public class TestCRC32C {
    
    
    
    
    
    
    
    
    
    final static long polyBits = (1L<<(32-32)) + (1L<<(32-28)) + (1L<<(32-27))
                               + (1L<<(32-26)) + (1L<<(32-25)) + (1L<<(32-23)) + (1L<<(32-22))
                               + (1L<<(32-20)) + (1L<<(32-19)) + (1L<<(32-18)) + (1L<<(32-14))
                               + (1L<<(32-13)) + (1L<<(32-11)) + (1L<<(32-10)) + (1L<<(32-9))
                               + (1L<<(32-8))  + (1L<<(32-6))  + (1L<<(32-0));
    final static long polyBitsShifted = polyBits>>1;

    public static void main(String[] args) throws Exception {
        int offset = Integer.getInteger("offset", 0);
        int msgSize = Integer.getInteger("msgSize", 512);
        boolean multi = false;
        int iters = 20000;
        int warmupIters = 20000;

        if (args.length > 0) {
            if (args[0].equals("-m")) {
                multi = true;
            } else {
                iters = Integer.valueOf(args[0]);
            }
            if (args.length > 1) {
                warmupIters = Integer.valueOf(args[1]);
            }
        }

        if (multi) {
            test_multi(warmupIters);
            return;
        }

        System.out.println(" offset = " + offset);
        System.out.println("msgSize = " + msgSize + " bytes");
        System.out.println("  iters = " + iters);

        byte[] b = initializedBytes(msgSize, offset);

        final long crcReference = update_byteLoop(0, b, offset);

        CRC32C crc0 = new CRC32C();
        CRC32C crc1 = new CRC32C();
        CRC32C crc2 = new CRC32C();

        crc0.update(b, offset, msgSize);
        check(crc0, crcReference);

        System.out.println("-------------------------------------------------------");

        
        for (int i = 0; i < warmupIters; i++) {
            crc1.reset();
            crc1.update(b, offset, msgSize);
            check(crc1, crcReference);
        }

        
        for (int i = 0; i < iters; i++) {
            crc1.reset();
            crc1.update(b, offset, msgSize);
            check(crc1, crcReference);
        }
        report("CRCs", crc1, crcReference);

        
        long start = System.nanoTime();
        for (int i = 0; i < iters; i++) {
            crc1.reset();
            crc1.update(b, offset, msgSize);
        }
        long end = System.nanoTime();

        double total = (double)(end - start)/1e9;         
        double thruput = (double)msgSize*iters/1e6/total; 
        System.out.println("CRC32C.update(byte[]) runtime = " + total + " seconds");
        System.out.println("CRC32C.update(byte[]) throughput = " + thruput + " MB/s");
        report("CRCs", crc1, crcReference);

        System.out.println("-------------------------------------------------------");

        ByteBuffer buf = ByteBuffer.allocateDirect(msgSize);
        buf.put(b, offset, msgSize);
        buf.flip();

        
        for (int i = 0; i < warmupIters; i++) {
            crc2.reset();
            crc2.update(buf);
            buf.rewind();
            check(crc2, crcReference);
        }

        
        for (int i = 0; i < iters; i++) {
            crc2.reset();
            crc2.update(buf);
            buf.rewind();
            check(crc2, crcReference);
        }
        report("CRCs", crc2, crcReference);

        
        start = System.nanoTime();
        for (int i = 0; i < iters; i++) {
            crc2.reset();
            crc2.update(buf);
            buf.rewind();
        }
        end = System.nanoTime();
        total = (double)(end - start)/1e9;         
        thruput = (double)msgSize*iters/1e6/total; 
        System.out.println("CRC32C.update(ByteBuffer) runtime = " + total + " seconds");
        System.out.println("CRC32C.update(ByteBuffer) throughput = " + thruput + " MB/s");
        report("CRCs", crc2, crcReference);

        System.out.println("-------------------------------------------------------");
    }

    
    public static long update_byteLoop(long crc, byte[] buf, int offset) {
        return update_byteLoop(crc, buf, offset, buf.length-offset);
    }

    
    public static long update_byteLoop(long crc, byte[] buf, int offset, int length) {
        int end = length+offset;
        for (int i = offset; i < end; i++) {
            crc = update_singlebyte(crc, polyBitsShifted, buf[i]);
        }
        return crc;
    }

    
    
    
    
    
    public static long update_singlebyte(long crc, long polynomial, int val) {
        crc = (crc ^ -1L) & 0x00000000ffffffffL;  
        crc =  crc ^ (val&0xff);                  
        for (int i = 0; i <  8; i++) {
            boolean bitset = (crc & 0x01L) != 0;

            crc = crc>>1;
            if (bitset) {
                crc = crc ^ polynomial;
                crc = crc & 0x00000000ffffffffL;
            }
        }
        crc = (crc ^ -1L) & 0x00000000ffffffffL;  
        return crc;
    }

    private static void report(String s, Checksum crc, long crcReference) {
        System.out.printf("%s: crc = %08x, crcReference = %08x\n",
                          s, crc.getValue(), crcReference);
    }

    private static void check(Checksum crc, long crcReference) throws Exception {
        if (crc.getValue() != crcReference) {
            System.err.printf("ERROR: crc = %08x, crcReference = %08x\n",
                              crc.getValue(), crcReference);
            throw new Exception("TestCRC32C Error");
        }
    }

    private static byte[] initializedBytes(int M, int offset) {
        byte[] bytes = new byte[M + offset];
        for (int i = 0; i < offset; i++) {
            bytes[i] = (byte) i;
        }
        for (int i = offset; i < bytes.length; i++) {
            bytes[i] = (byte) (i - offset);
        }
        return bytes;
    }

    private static void test_multi(int iters) throws Exception {
        int len1 = 8;    
        int len2 = 32;   
        int len3 = 4096; 

        byte[] b = initializedBytes(len3*16, 0);
        int[] offsets = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 16, 32, 64, 128, 256, 512 };
        int[] sizes = { 0, 1, 2, 3, 4, 5, 6, 7,
                        len1, len1+1, len1+2, len1+3, len1+4, len1+5, len1+6, len1+7,
                        len1*2, len1*2+1, len1*2+3, len1*2+5, len1*2+7,
                        len2, len2+1, len2+3, len2+5, len2+7,
                        len2*2, len2*4, len2*8, len2*16, len2*32, len2*64,
                        len3, len3+1, len3+3, len3+5, len3+7,
                        len3*2, len3*4, len3*8,
                        len1+len2, len1+len2+1, len1+len2+3, len1+len2+5, len1+len2+7,
                        len1+len3, len1+len3+1, len1+len3+3, len1+len3+5, len1+len3+7,
                        len2+len3, len2+len3+1, len2+len3+3, len2+len3+5, len2+len3+7,
                        len1+len2+len3, len1+len2+len3+1, len1+len2+len3+3,
                        len1+len2+len3+5, len1+len2+len3+7,
                        (len1+len2+len3)*2, (len1+len2+len3)*2+1, (len1+len2+len3)*2+3,
                        (len1+len2+len3)*2+5, (len1+len2+len3)*2+7,
                        (len1+len2+len3)*3, (len1+len2+len3)*3-1, (len1+len2+len3)*3-3,
                        (len1+len2+len3)*3-5, (len1+len2+len3)*3-7 };
        CRC32C[] crc1 = new CRC32C[offsets.length*sizes.length];
        long[] crcReference = new long[offsets.length*sizes.length];
        int i, j, k;

        System.out.printf("testing %d cases ...\n", offsets.length*sizes.length);

        
        
        for (i = 0; i < offsets.length; i++) {
            for (j = 0; j < sizes.length; j++) {
                crc1[i*sizes.length + j] = new CRC32C();
                crcReference[i*sizes.length + j] = update_byteLoop(0, b, offsets[i], sizes[j]);
            }
        }

        
        
        
        
        for (k = 0; k < iters; k++) {
            for (i = 0; i < offsets.length; i++) {
                for (j = 0; j < sizes.length; j++) {
                    crc1[i*sizes.length + j].reset();
                    crc1[i*sizes.length + j].update(b, offsets[i], sizes[j]);
                    check(crc1[i*sizes.length + j], crcReference[i*sizes.length + j]);
                }
            }
        }
    }
}
