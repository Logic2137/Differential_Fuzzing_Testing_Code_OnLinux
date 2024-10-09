



import java.util.*;
import java.nio.*;
import java.nio.charset.*;

public class TestStringCoding {
    public static void main(String[] args) throws Throwable {

        
        char[] bmp = new char[0x10000];
        for (int i = 0; i < 0x10000; i++) {
            bmp[i] = (char)i;
        }
        char[] latin = Arrays.copyOf(bmp, 0x100);
        char[] ascii =  Arrays.copyOf(bmp, 0x80);

        byte[] latinBA = new byte[0x100];
        for (int i = 0; i < 0x100; i++) {
            latinBA[i] = (byte)i;
        }
        byte[] asciiBA =  Arrays.copyOf(latinBA, 0x80);

        for (Boolean hasSM: new boolean[] { false, true }) {
            if (hasSM) {
                System.setSecurityManager(new PermissiveSecurityManger());
            }
            for (Charset cs:  Charset.availableCharsets().values()) {
                if ("ISO-2022-CN".equals(cs.name()) ||
                    "x-COMPOUND_TEXT".equals(cs.name()) ||
                    "x-JISAutoDetect".equals(cs.name()))
                    continue;
                System.out.printf("Testing(sm=%b) " + cs.name() + "....", hasSM);

                testNewString(cs, testGetBytes(cs, new String(bmp)));
                testNewString(cs, testGetBytes(cs, new String(latin)));
                testNewString(cs, testGetBytes(cs, new String(ascii)));
                testGetBytes(cs, testNewString(cs, latinBA));
                testGetBytes(cs, testNewString(cs, asciiBA));

                
                Random rnd = new Random();
                for (int i = 0; i < 10; i++) {
                    
                    char[] bmp0 = Arrays.copyOf(bmp, rnd.nextInt(0x10000));
                    testNewString(cs, testGetBytes(cs, new String(bmp0)));
                    
                    int pos = bmp0.length / 2;
                    if ((pos + 1) < bmp0.length) {
                        bmp0[pos] = '\uD800';
                        bmp0[pos+1] = '\uDC00';
                    }
                    testNewString(cs, testGetBytes(cs, new String(bmp0)));

                    char[] latin0 = Arrays.copyOf(latin, rnd.nextInt(0x100));
                    char[] ascii0 = Arrays.copyOf(ascii, rnd.nextInt(0x80));
                    byte[] latinBA0 = Arrays.copyOf(latinBA, rnd.nextInt(0x100));
                    byte[] asciiBA0 = Arrays.copyOf(asciiBA, rnd.nextInt(0x80));
                    testNewString(cs, testGetBytes(cs, new String(latin0)));
                    testNewString(cs, testGetBytes(cs, new String(ascii0)));
                    testGetBytes(cs, testNewString(cs, latinBA0));
                    testGetBytes(cs, testNewString(cs, asciiBA0));
                }
                testSurrogates(cs);
                testMixed(cs);
                System.out.println("done!");
            }
        }
    }

    static void testMixed(Charset cs) throws Throwable {
        CharsetDecoder dec = cs.newDecoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);
        CharsetEncoder enc = cs.newEncoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);
        List<Integer> cps = new ArrayList<>(0x10000);
        int off = 0;
        int cp = 0;
        while (cp < 0x10000) {
            if (enc.canEncode((char)cp)) {
               cps.add(cp);
            }
            cp++;
        }
        Collections.shuffle(cps);
        char[] bmpCA = new char[cps.size()];
        for (int i = 0; i < cps.size(); i++)
            bmpCA[i] = (char)(int)cps.get(i);
        String bmpStr = new String(bmpCA);
        
        byte[] bmpBA = bmpStr.getBytes(cs.name());
        ByteBuffer bf = enc.reset().encode(CharBuffer.wrap(bmpCA));
        byte[] baNIO = new byte[bf.limit()];
        bf.get(baNIO, 0, baNIO.length);
        if (!Arrays.equals(bmpBA, baNIO)) {
            throw new RuntimeException("getBytes(csn) failed  -> " + cs.name());
        }

        
        bmpBA = bmpStr.getBytes(cs);
        if (!Arrays.equals(bmpBA, baNIO)) {
            throw new RuntimeException("getBytes(cs) failed  -> " + cs.name());
        }

        
        String strSC = new String(bmpBA, cs.name());
        String strNIO = dec.reset().decode(ByteBuffer.wrap(bmpBA)).toString();
        if(!strNIO.equals(strSC)) {
            throw new RuntimeException("new String(csn) failed  -> " + cs.name());
        }
        
        strSC = new String(bmpBA, cs);
        if (!strNIO.equals(strSC)) {
            throw new RuntimeException("new String(cs) failed  -> " + cs.name());
        }
    }

    static byte[] getBytes(CharsetEncoder enc, String str) throws Throwable {
        ByteBuffer bf = enc.reset().encode(CharBuffer.wrap(str.toCharArray()));
        byte[] ba = new byte[bf.limit()];
        bf.get(ba, 0, ba.length);
        return ba;
    }

    static byte[] testGetBytes(Charset cs, String str) throws Throwable {
        CharsetEncoder enc = cs.newEncoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);
        
        byte[] baSC = str.getBytes(cs.name());
        byte[] baNIO = getBytes(enc, str);
        if (!Arrays.equals(baSC, baNIO)) {
            throw new RuntimeException("getBytes(csn) failed  -> " + cs.name());
        }
        
        baSC = str.getBytes(cs);
        if (!Arrays.equals(baSC, baNIO)) {
            throw new RuntimeException("getBytes(cs) failed  -> " + cs.name());
        }
        return baSC;
    }

    static String testNewString(Charset cs, byte[] ba) throws Throwable {
        CharsetDecoder dec = cs.newDecoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);
        
        String strSC = new String(ba, cs.name());
        String strNIO = dec.reset().decode(ByteBuffer.wrap(ba)).toString();
        if(!strNIO.equals(strSC)) {
            throw new RuntimeException("new String(csn) failed  -> " + cs.name());
        }
        
        strSC = new String(ba, cs);
        if (!strNIO.equals(strSC)) {
            throw new RuntimeException("new String(cs)/bmp failed  -> " + cs.name());
        }
        return strSC;
    }

    static void testSurrogates(Charset cs) throws Throwable {
        
        CharsetEncoder enc = cs.newEncoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);
        if (enc instanceof sun.nio.cs.ArrayEncoder &&
            cs.contains(Charset.forName("ASCII"))) {
            if (cs.name().equals("UTF-8") ||     
                cs.name().equals("CESU-8"))      
                return;
            enc.replaceWith(new byte[] { (byte)'A'});
            sun.nio.cs.ArrayEncoder cae = (sun.nio.cs.ArrayEncoder)enc;

            String str = "ab\uD800\uDC00\uD800\uDC00cd";
            byte[] ba = new byte[str.length() - 2];
            int n = cae.encode(str.toCharArray(), 0, str.length(), ba);
            if (n != 6 || !"abAAcd".equals(new String(ba, cs.name())))
                throw new RuntimeException("encode1(surrogates) failed  -> "
                                           + cs.name());

            ba = new byte[str.length()];
            n = cae.encode(str.toCharArray(), 0, str.length(), ba);
            if (n != 6 || !"abAAcd".equals(new String(ba, 0, n,
                                                     cs.name())))
                throw new RuntimeException("encode2(surrogates) failed  -> "
                                           + cs.name());
            str = "ab\uD800B\uDC00Bcd";
            ba = new byte[str.length()];
            n = cae.encode(str.toCharArray(), 0, str.length(), ba);
            if (n != 8 || !"abABABcd".equals(new String(ba, 0, n,
                                                       cs.name())))
                throw new RuntimeException("encode3(surrogates) failed  -> "
                                           + cs.name());
            
        }

        
        if (cs.name().equals("Big5-HKSCS") || cs.name().equals("x-MS950-HKSCS")) {
            String str = "ab\uD840\uDD0Ccd";
            byte[] expected = new byte[] {(byte)'a', (byte)'b',
                (byte)0x88, (byte)0x45, (byte)'c', (byte)'d' };
            if (!Arrays.equals(str.getBytes(cs.name()), expected) ||
                !Arrays.equals(str.getBytes(cs), expected)) {
                throw new RuntimeException("encode(surrogates) failed  -> "
                                           + cs.name());
            }
        }
    }

    static class PermissiveSecurityManger extends SecurityManager {
        @Override public void checkPermission(java.security.Permission p) {}
    }
}
