



import java.nio.charset.*;
import java.nio.*;
import java.util.*;

public class TestIBMDB {
    static class Time {
        long t;
    }
    static int iteration = 200;

    static char[] decode(byte[] bb, Charset cs, boolean testDirect, Time t)
        throws Exception {
        String csn = cs.name();
        CharsetDecoder dec = cs.newDecoder();
        ByteBuffer bbf;
        CharBuffer cbf;
        if (testDirect) {
            bbf = ByteBuffer.allocateDirect(bb.length);
            cbf = ByteBuffer.allocateDirect(bb.length*2).asCharBuffer();
            bbf.put(bb);
        } else {
            bbf = ByteBuffer.wrap(bb);
            cbf = CharBuffer.allocate(bb.length);
        }
        CoderResult cr = null;
        long t1 = System.nanoTime()/1000;
        for (int i = 0; i < iteration; i++) {
            bbf.rewind();
            cbf.clear();
            dec.reset();
            cr = dec.decode(bbf, cbf, true);
        }
        long t2 = System.nanoTime()/1000;
        t.t = (t2 - t1)/iteration;
        if (cr != CoderResult.UNDERFLOW) {
            System.out.println("DEC-----------------");
            int pos = bbf.position();
            System.out.printf("  cr=%s, bbf.pos=%d, bb[pos]=%x,%x,%x,%x%n",
                              cr.toString(), pos,
                              bb[pos++]&0xff, bb[pos++]&0xff,bb[pos++]&0xff, bb[pos++]&0xff);
            throw new RuntimeException("Decoding err: " + csn);
        }
        char[] cc = new char[cbf.position()];
        cbf.flip(); cbf.get(cc);
        return cc;

    }

    static CoderResult decodeCR(byte[] bb, Charset cs, boolean testDirect)
        throws Exception {
        CharsetDecoder dec = cs.newDecoder();
        ByteBuffer bbf;
        CharBuffer cbf;
        if (testDirect) {
            bbf = ByteBuffer.allocateDirect(bb.length);
            cbf = ByteBuffer.allocateDirect(bb.length*2).asCharBuffer();
            bbf.put(bb).flip();
        } else {
            bbf = ByteBuffer.wrap(bb);
            cbf = CharBuffer.allocate(bb.length);
        }
        CoderResult cr = null;
        for (int i = 0; i < iteration; i++) {
            bbf.rewind();
            cbf.clear();
            dec.reset();
            cr = dec.decode(bbf, cbf, true);
        }
        return cr;
    }

    static byte[] encode(char[] cc, Charset cs, boolean testDirect, Time t)
        throws Exception {
        ByteBuffer bbf;
        CharBuffer cbf;
        CharsetEncoder enc = cs.newEncoder();
        String csn = cs.name();
        if (testDirect) {
            bbf = ByteBuffer.allocateDirect(cc.length * 4);
            cbf = ByteBuffer.allocateDirect(cc.length * 2).asCharBuffer();
            cbf.put(cc).flip();
        } else {
            bbf = ByteBuffer.allocate(cc.length * 4);
            cbf = CharBuffer.wrap(cc);
        }
        CoderResult cr = null;
        long t1 = System.nanoTime()/1000;
        for (int i = 0; i < iteration; i++) {
            cbf.rewind();
            bbf.clear();
            enc.reset();
            cr = enc.encode(cbf, bbf, true);
        }
        long t2 = System.nanoTime()/1000;
        t.t = (t2 - t1)/iteration;
        if (cr != CoderResult.UNDERFLOW) {
            System.out.println("ENC-----------------");
            int pos = cbf.position();
            System.out.printf("  cr=%s, cbf.pos=%d, cc[pos]=%x%n",
                              cr.toString(), pos, cc[pos]&0xffff);
            throw new RuntimeException("Encoding err: " + csn);
        }
        byte[] bb = new byte[bbf.position()];
        bbf.flip(); bbf.get(bb);
        return bb;
    }

    static CoderResult encodeCR(char[] cc, Charset cs, boolean testDirect)
        throws Exception {
        ByteBuffer bbf;
        CharBuffer cbf;
        CharsetEncoder enc = cs.newEncoder();
        if (testDirect) {
            bbf = ByteBuffer.allocateDirect(cc.length * 4);
            cbf = ByteBuffer.allocateDirect(cc.length * 2).asCharBuffer();
            cbf.put(cc).flip();
        } else {
            bbf = ByteBuffer.allocate(cc.length * 4);
            cbf = CharBuffer.wrap(cc);
        }
        CoderResult cr = null;
        for (int i = 0; i < iteration; i++) {
            cbf.rewind();
            bbf.clear();
            enc.reset();
            cr = enc.encode(cbf, bbf, true);
        }
        return cr;
    }

    static void printEntry(char c, Charset cs) {
        byte[] bb = new String(new char[] {c}).getBytes(cs);
        for (byte b:bb)
            System.out.printf("%x", b&0xff);
        System.out.printf("    %x", c & 0xffff);
        String s2 = new String(bb, cs);
        System.out.printf("    %x%n", s2.charAt(0) & 0xffff);
    }

    
    static char[] checkEncoding(Charset oldCS, Charset newCS)
        throws Exception {
        System.out.printf("Encoding <%s> <%s>...%n", oldCS.name(), newCS.name());
        CharsetEncoder encOLD = oldCS.newEncoder();
        CharsetEncoder encNew = newCS.newEncoder();
        char[] cc = new char[0x10000];
        int pos = 0;
        boolean is970 = "x-IBM970-Old".equals(oldCS.name());

        for (char c = 0; c < 0xffff; c++) {
            boolean canOld = encOLD.canEncode(c);
            boolean canNew = encNew.canEncode(c);

            if (is970 && c == 0x2299)
                continue;

            if (canOld != canNew) {
                if (canNew) {
                    System.out.printf("      NEW(only): ");
                    printEntry(c, newCS);
                } else {
                    if (is970) {
                        byte[] bb = new String(new char[] {c}).getBytes(oldCS);
                        if (bb.length == 2 && bb[0] == (byte)0xa2 && bb[1] == (byte)0xc1) {
                        
                            continue;
                        }
                    }
                    System.out.printf("      OLD(only): ");
                    printEntry(c, oldCS);
                }
            } else if (canNew) {
                byte[] bbNew = new String(new char[] {c}).getBytes(newCS);
                byte[] bbOld = new String(new char[] {c}).getBytes(oldCS);
                if (!Arrays.equals(bbNew, bbOld)) {
                    System.out.printf("      c->b NEW: ");
                    printEntry(c, newCS);
                    System.out.printf("      c->b OLD: ");
                    printEntry(c, oldCS);
                } else {
                    String sNew = new String(bbNew, newCS);
                    String sOld = new String(bbOld, oldCS);
                    if (!sNew.equals(sOld)) {
                        System.out.printf("      b2c NEW (c=%x):", c&0xffff);
                        printEntry(sNew.charAt(0), newCS);
                        System.out.printf("      b2c OLD:");
                        printEntry(sOld.charAt(0), oldCS);
                    }
                }
            }
            if (canNew & canOld) {  
                cc[pos++] = c;
            }
        }
        return Arrays.copyOf(cc, pos);
    }


    
    static void checkDecoding(Charset oldCS, Charset newCS)
        throws Exception
    {
        System.out.printf("Decoding <%s> <%s>...%n", oldCS.name(), newCS.name());
        boolean isEBCDIC = oldCS.name().startsWith("x-IBM93");

        
        byte[] bb = new byte[1];
        System.out.printf("       trying SB...%n");
        for (int b = 0; b < 0x100; b++) {
            bb[0] = (byte)b;
            String sOld = new String(bb, oldCS);
            String sNew = new String(bb, newCS);
            if (!sOld.equals(sNew)) {
                System.out.printf("        b=%x:  %x/%d(old)  %x/%d(new)%n",
                                  b& 0xff,
                                  sOld.charAt(0) & 0xffff, sOld.length(),
                                  sNew.charAt(0) & 0xffff, sNew.length());
            }
        }

        System.out.printf("       trying DB...%n");
        bb = new byte[isEBCDIC?4:2];
        int b1Min = 0x40;
        int b1Max = 0xfe;
        for (int b1 = 0x40; b1 < 0xff; b1++) {
            if (!isEBCDIC) {
                
                bb[0] = (byte)b1;
                String sOld = new String(bb, oldCS);
                String sNew = new String(bb, newCS);
                if (!sOld.equals(sNew)) {
                    if (sOld.length() != 2 && sOld.charAt(0) != 0) {
                        
                        System.out.printf("        b1=%x:  %x/%d(old)  %x/%d(new)%n",
                                          b1 & 0xff,
                                          sOld.charAt(0) & 0xffff, sOld.length(),
                                          sNew.charAt(0) & 0xffff, sNew.length());
                        continue;
                    }
                }
            }
            for (int b2 = 0x40; b2 < 0xff; b2++) {
                if (isEBCDIC) {
                    bb[0] = 0x0e;
                    bb[1] = (byte)b1;
                    bb[2] = (byte)b2;
                    bb[3] = 0x0f;
                } else {
                    bb[0] = (byte)b1;
                    bb[1] = (byte)b2;
                }
                String sOld = new String(bb, oldCS);
                String sNew = new String(bb, newCS);
                
                if (sOld.charAt(0) != sNew.charAt(0)) {

if (sOld.charAt(0) == 0 && sNew.charAt(0) == 0xfffd)
    continue; 

                    System.out.printf("        bb=<%x,%x>  c(old)=%x,  c(new)=%x%n",
                        b1, b2, sOld.charAt(0) & 0xffff, sNew.charAt(0) & 0xffff);
                }
            }
        }
    }

    static void checkInit(String csn) throws Exception {
        System.out.printf("Check init <%s>...%n", csn);
        Charset.forName("Big5");    
        long t1 = System.nanoTime()/1000;
        Charset cs = Charset.forName(csn);
        long t2 = System.nanoTime()/1000;
        System.out.printf("    charset     :%d%n", t2 - t1);
        t1 = System.nanoTime()/1000;
            cs.newDecoder();
        t2 = System.nanoTime()/1000;
        System.out.printf("    new Decoder :%d%n", t2 - t1);

        t1 = System.nanoTime()/1000;
            cs.newEncoder();
        t2 = System.nanoTime()/1000;
        System.out.printf("    new Encoder :%d%n", t2 - t1);
    }

    static void compare(Charset cs1, Charset cs2, char[] cc) throws Exception {
        System.gc();    
        Thread.sleep(1000);
        System.gc();    

        String csn1 = cs1.name();
        String csn2 = cs2.name();
        System.out.printf("Diff     <%s> <%s>...%n", csn1, csn2);

        Time t1 = new Time();
        Time t2 = new Time();

        byte[] bb1 = encode(cc, cs1, false, t1);
        byte[] bb2 = encode(cc, cs2, false, t2);

        System.out.printf("    Encoding TimeRatio %s/%s: %d,%d :%f%n",
                          csn2, csn1,
                          t2.t, t1.t,
                          (double)(t2.t)/(t1.t));
        if (!Arrays.equals(bb1, bb2)) {
            System.out.printf("        encoding failed%n");
        }

        char[] cc2 = decode(bb1, cs2, false, t2);
        char[] cc1 = decode(bb1, cs1, false, t1);
        System.out.printf("    Decoding TimeRatio %s/%s: %d,%d :%f%n",
                          csn2, csn1,
                          t2.t, t1.t,
                          (double)(t2.t)/(t1.t));
        if (!Arrays.equals(cc1, cc2)) {
            System.out.printf("        decoding failed%n");
        }

        bb1 = encode(cc, cs1, true, t1);
        bb2 = encode(cc, cs2, true, t2);

        System.out.printf("    Encoding(dir) TimeRatio %s/%s: %d,%d :%f%n",
                          csn2, csn1,
                          t2.t, t1.t,
                          (double)(t2.t)/(t1.t));

        if (!Arrays.equals(bb1, bb2))
            System.out.printf("        encoding (direct) failed%n");

        cc1 = decode(bb1, cs1, true, t1);
        cc2 = decode(bb1, cs2, true, t2);
        System.out.printf("    Decoding(dir) TimeRatio %s/%s: %d,%d :%f%n",
                          csn2, csn1,
                          t2.t, t1.t,
                          (double)(t2.t)/(t1.t));
        if (!Arrays.equals(cc1, cc2)) {
            System.out.printf("        decoding (direct) failed%n");
        }
    }

    

    static void checkMalformed(Charset cs, byte[][] malformed)
        throws Exception
    {
        boolean failed = false;
        String csn = cs.name();
        System.out.printf("Check malformed <%s>...%n", csn);
        for (boolean direct: new boolean[] {false, true}) {
            for (byte[] bins : malformed) {
                int mlen = bins[0];
                byte[] bin = Arrays.copyOfRange(bins, 1, bins.length);
                CoderResult cr = decodeCR(bin, cs, direct);
                String ashex = "";
                for (int i = 0; i < bin.length; i++) {
                    if (i > 0) ashex += " ";
                        ashex += Integer.toString((int)bin[i] & 0xff, 16);
                }
                if (!cr.isMalformed()) {
                    System.out.printf("        FAIL(direct=%b): [%s] not malformed. -->cr=%s\n", direct, ashex, cr.toString());
                    failed = true;
                } else if (cr.length() != mlen) {
                    System.out.printf("        FAIL(direct=%b): [%s] malformed[len=%d].\n", direct, ashex, cr.length());
                    failed = true;
                }
            }
        }
        if (failed)
            throw new RuntimeException("Check malformed failed " + csn);
    }

    static boolean check(CharsetDecoder dec, byte[] bytes, boolean direct, int[] flow) {
        int inPos = flow[0];
        int inLen = flow[1];
        int outPos = flow[2];
        int outLen = flow[3];
        int expedInPos = flow[4];
        int expedOutPos = flow[5];
        CoderResult expedCR = (flow[6]==0)?CoderResult.UNDERFLOW
                                          :CoderResult.OVERFLOW;
        ByteBuffer bbf;
        CharBuffer cbf;
        if (direct) {
            bbf = ByteBuffer.allocateDirect(inPos + bytes.length);
            cbf = ByteBuffer.allocateDirect((outPos + outLen)*2).asCharBuffer();
        } else {
            bbf = ByteBuffer.allocate(inPos + bytes.length);
            cbf = CharBuffer.allocate(outPos + outLen);
        }
        bbf.position(inPos);
        bbf.put(bytes).flip().position(inPos).limit(inPos + inLen);
        cbf.position(outPos);
        dec.reset();
        CoderResult cr = dec.decode(bbf, cbf, false);
        if (cr != expedCR ||
            bbf.position() != expedInPos ||
            cbf.position() != expedOutPos) {
            System.out.printf("Expected(direct=%5b): [", direct);
            for (int i:flow) System.out.print(" " + i);
            System.out.println("]  CR=" + cr +
                               ", inPos=" + bbf.position() +
                               ", outPos=" + cbf.position());
            return false;
        }
        return true;
    }

    static void checkUnderOverflow(Charset cs) throws Exception {
        String csn = cs.name();
        System.out.printf("Check under/overflow <%s>...%n", csn);
        CharsetDecoder dec = cs.newDecoder();
        boolean failed = false;

        
        
        byte[] bytes = new String("\u007f\u3000\u4e42\u4e28\ud840\udc55").getBytes("EUC_TW");
        int    inlen = bytes.length;

        int MAXOFF = 20;
        for (int inoff = 0; inoff < MAXOFF; inoff++) {
            for (int outoff = 0; outoff < MAXOFF; outoff++) {
        int[][] Flows = {
            
            
            {inoff,  inlen, outoff,  1,      inoff + 1,  outoff + 1, 1},
            {inoff,  inlen, outoff,  2,      inoff + 3,  outoff + 2, 1},
            {inoff,  inlen, outoff,  3,      inoff + 7,  outoff + 3, 1},
            {inoff,  inlen, outoff,  4,      inoff + 11, outoff + 4, 1},
            {inoff,  inlen, outoff,  5,      inoff + 11, outoff + 4, 1},
            {inoff,  inlen, outoff,  6,      inoff + 15, outoff + 6, 0},
            
            {inoff,  1,     outoff,  6,      inoff + 1,  outoff + 1, 0},
            {inoff,  2,     outoff,  6,      inoff + 1,  outoff + 1, 0},
            {inoff,  3,     outoff,  6,      inoff + 3,  outoff + 2, 0},
            {inoff,  4,     outoff,  6,      inoff + 3,  outoff + 2, 0},
            {inoff,  5,     outoff,  6,      inoff + 3,  outoff + 2, 0},
            {inoff,  8,     outoff,  6,      inoff + 7,  outoff + 3, 0},
            {inoff,  9,     outoff,  6,      inoff + 7,  outoff + 3, 0},
            {inoff, 10,     outoff,  6,      inoff + 7,  outoff + 3, 0},
            {inoff, 11,     outoff,  6,      inoff +11,  outoff + 4, 0},
            {inoff, 12,     outoff,  6,      inoff +11,  outoff + 4, 0},
            {inoff, 15,     outoff,  6,      inoff +15,  outoff + 6, 0},
            
            {inoff,  2,     outoff,  1,      inoff + 1,  outoff + 1, 0},
            {inoff,  3,     outoff,  1,      inoff + 1,  outoff + 1, 1},
            {inoff,  3,     outoff,  2,      inoff + 3,  outoff + 2, 0},
        };
        for (boolean direct: new boolean[] {false, true}) {
            for (int[] flow: Flows) {
                if (!check(dec, bytes, direct, flow))
                    failed = true;
            }
        }}}
        if (failed)
            throw new RuntimeException("Check under/overflow failed " + csn);
    }

    static String[] csnames = new String[] {

        "IBM930",
        "IBM933",
        "IBM935",
        "IBM937",
        "IBM939",
        "IBM942",
        "IBM943",
        "IBM948",
        "IBM949",
        "IBM950",
        "IBM970",
        "IBM942C",
        "IBM943C",
        "IBM949C",
        "IBM1381",
        "IBM1383",

        "EUC_CN",
        "EUC_KR",
        "GBK",
        "Johab",
        "MS932",
        "MS936",
        "MS949",
        "MS950",

        "EUC_JP",
        "EUC_JP_LINUX",
        "EUC_JP_Open",
        "SJIS",
        "PCK",
    };

    public static void main(String[] args) throws Exception {
        for (String csname: csnames) {
            System.out.printf("-----------------------------------%n");
            String oldname = csname + "_OLD";
            if ("EUC_JP_Open".equals(csname))
                csname = "eucjp-open";
            checkInit(csname);
            Charset csOld = (Charset)Class.forName(oldname).newInstance();
            Charset csNew = Charset.forName(csname);
            char[] cc = checkEncoding(csOld, csNew);
            checkDecoding(csOld, csNew);
            compare(csNew, csOld, cc);

            if (csname.startsWith("x-IBM93")) {
                
                checkMalformed(csNew, new byte[][] {
                    {1, 0x26, 0x0f, 0x27},         
                    {1, 0x0e, 0x41, 0x41, 0xe},    
                    {2, 0x0e, 0x40, 0x41, 0xe},    
                });
            } else if (csname.equals("x-IBM970") ||
                       csname.equals("x-IBM1383")) {
                
                checkMalformed(csNew, new byte[][] {
                    {1, 0x26, (byte)0x8f, 0x27},                   
                    {1, (byte)0xa1, (byte)0xa1, (byte)0x8e, 0x51}, 
                });
            }
        }
    }
}
