



import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.Arrays;

public class TestIBMBugs {

    private static void bug6371437() throws Exception {
        CharsetEncoder converter = Charset.forName("Cp933").newEncoder();
        converter = converter.onMalformedInput(CodingErrorAction.REPORT);
        converter = converter.onUnmappableCharacter(CodingErrorAction.REPORT);
        CharBuffer in = CharBuffer.wrap(new char[] { (char)4352 });
        try {
              ByteBuffer out = converter.encode(in);
        } catch (CharacterCodingException e) { }
    }

    private static void bug6371422() throws Exception {
        String[] charsets = { "Cp949", "Cp949C" };
        for (int n = 0; n < charsets.length; n++) {
            String charset = charsets[n];
            CharsetEncoder converter = Charset.forName(charset).newEncoder();
            converter = converter.onMalformedInput(CodingErrorAction.REPORT);
            converter = converter.onUnmappableCharacter(CodingErrorAction.REPORT);
            int errors = 0;
            for (int i = 1; i < 0x1ffff; i++) {
                if (i >= 0x1100 && i <= 0x11f9)
                    continue;  
                               
                char[] in = (i < 0x10000
                         ? new char[] { (char)i }
                             : new char[] { (char)(0xd800 + ((i - 0x10000) >> 10)),
                              (char)(0xdc00 + ((i - 0x10000) & 0x3ff)) });

                try {
                    ByteBuffer out = converter.encode(CharBuffer.wrap(in));
                    if (out.remaining() == 0 ||
                        (out.remaining() == 1 && out.get(0) == 0x00)) {
                    errors++;
                    }
                } catch (CharacterCodingException e) { }
            }
            if (errors > 0)
                throw new Exception("Charset "+charset+": "+errors+" errors");
        }
    }

    private static void bug6371416() throws Exception {
        String[] charsets = { "Cp933", "Cp949", "Cp949C", "Cp970"};
        for (int n = 0; n < charsets.length; n++) {
            String charset = charsets[n];
            CharsetEncoder converter = Charset.forName(charset).newEncoder();
            converter = converter.onMalformedInput(CodingErrorAction.REPORT);
            converter = converter.onUnmappableCharacter(CodingErrorAction.REPORT);
            int errors = 0;
            for (int i = 0xd800; i < 0xe000; i++) {
                char[] in = new char[] { (char)i };
                try {
                    ByteBuffer out = converter.encode(CharBuffer.wrap(in));
                    if (out.remaining() == 0)
                        errors++;
                } catch (CharacterCodingException e) { }
            }
            if (errors > 0)
                throw new Exception("Charset "+charset+": "+errors+" errors");
        }
    }

    private static void bug6371619() throws Exception {
        String encoding = "Cp964";
        Charset charset = Charset.forName(encoding);
        CharsetDecoder converter = charset.newDecoder();
        converter = converter.onMalformedInput(CodingErrorAction.REPORT);
        converter = converter.onUnmappableCharacter(CodingErrorAction.REPORT);
        int errors = 0;
        for (int b = 0x80; b < 0x100; b++)
            if (!(b == 0x8e ||  
                  (b >= 0x80 && b <= 0x8d) || (b >= 0x90 && b <= 0x9f))) {
                ByteBuffer in = ByteBuffer.wrap(new byte[] { (byte)b });
                try {
                    CharBuffer out = converter.decode(in);
                    if (out.length() == 0) {
                        errors++;
                    }
                } catch (CharacterCodingException e) { }
            }
        if (errors > 0)
            throw new Exception("Charset "+charset+": "+errors+" errors");
    }


    private static void bug6371431() throws Exception {
        String encoding = "Cp33722";
        Charset charset = Charset.forName(encoding);
        CharsetDecoder converter = charset.newDecoder();
        converter = converter.onMalformedInput(CodingErrorAction.REPORT);
        converter = converter.onUnmappableCharacter(CodingErrorAction.REPORT);
        int errors = 0;
        for (int b = 0xa0; b < 0x100; b++) {
            ByteBuffer in = ByteBuffer.wrap(new byte[] { (byte)b });
            try {
                CharBuffer out = converter.decode(in);
                if (out.length() == 0) {
                    errors++;
                }
            } catch (CharacterCodingException e) { }
        }
        if (errors > 0)
            throw new Exception("Charset "+charset+": "+errors+" errors");
    }

    private static void bug6639450 () throws Exception {
        byte[] bytes1 = "\\".getBytes("IBM949");
        "\\".getBytes("IBM949C");
        byte[] bytes2 = "\\".getBytes("IBM949");
        if (bytes1.length != 1 || bytes2.length != 1 ||
            bytes1[0] != (byte)0x82 ||
            bytes2[0] != (byte)0x82)
        throw new Exception("IBM949/IBM949C failed");
    }

    private static void bug6569191 () throws Exception {
        byte[] bs = new byte[] { (byte)0x81, (byte)0xad,  
                                 (byte)0x81, (byte)0xae,  
                                 (byte)0x81, (byte)0xaf,  
                                 (byte)0x81, (byte)0xb0,  
                                 (byte)0x85, (byte)0x81,  
                                 (byte)0x85, (byte)0x87,  
                                 (byte)0x85, (byte)0xe0,  
                                 (byte)0x85, (byte)0xf0 };
        String s = new String(bs, "Cp943");
        
        if (!"\ufffd\uff6d\ufffd\uff6e\ufffd\uff6f\ufffd\uff70\ufffd\u2266\u32a4\u7165\ufffd"
            .equals(s))
            throw new Exception("Cp943 failed");
    }


    private static void bug6577466 () throws Exception {
        for (int c = Character.MIN_VALUE; c <= Character.MAX_VALUE; c++){
            if (!Character.isDefined((char)c)) continue;
            String s = String.valueOf((char)c);
            byte[] bb = null;
            bb = s.getBytes("x-IBM970");
        }
    }

    private static void bug8213618 () throws Exception {
        String cs = "x-IBM970";
        byte[] ba = new byte[]{(byte)0xA2,(byte)0xC1};
        String s = "\u25C9";
        if (!(new String(ba, cs)).equals(s))
            throw new Exception("Cp970 failed");
        if (!Arrays.equals(ba, s.getBytes(cs)))
            throw new Exception("Cp970 failed");
        ba = new byte[]{0x3f,0x3f,0x3f};
        if (!Arrays.equals(ba, "\u6950\u84f1\ucf7f".getBytes(cs)))
            throw new Exception("Cp970 failed");
    }

    private static void bug8202329() throws Exception {
        String original = "\\\u007E\u00A5\u203E"; 
        byte[] expectedBytes; 
        String expectedStringfromBytes; 

        Charset charset; 

        ByteBuffer bb; 
        byte[]  ba; 

        CharBuffer cb; 


        
        
        charset = Charset.forName("IBM943");
        expectedBytes = new byte[] {0x3f, 0x3f, 0x5c, 0x7e};
        expectedStringfromBytes = "??\u00A5\u203E";
        bb = charset.encode(original);
        ba = new byte[bb.remaining()];
        bb.get(ba, 0, ba.length);
        if(!Arrays.equals(ba, expectedBytes)) {
            throw new Exception("IBM943 failed to encode");
        }
        cb = charset.decode(ByteBuffer.wrap(expectedBytes));
        if(!cb.toString().equals(expectedStringfromBytes)) {
            throw new Exception("IBM943 failed to decode");
        }


        
        
        charset = Charset.forName("IBM943C");
        expectedBytes = new byte[] {0x5c, 0x7e, 0x5c, 0x7e};
        expectedStringfromBytes = "\\~\\~";
        bb = charset.encode(original);
        ba = new byte[bb.remaining()];
        bb.get(ba, 0, ba.length);
        if(!Arrays.equals(ba, expectedBytes)) {
            throw new Exception("IBM943C failed to encode");
        }
        cb = charset.decode(ByteBuffer.wrap(expectedBytes));
        if(!cb.toString().equals(expectedStringfromBytes)) {
            throw new Exception("IBM943C failed to decode");
        }
    }

    private static void bug8212794 () throws Exception {
        Charset cs = Charset.forName("x-IBM964");
        byte[] ba = new byte[] {(byte)0x5c, (byte)0x90, (byte)0xa1, (byte)0xa1};
        char[] ca = new char[] {'\\', '\u0090', '\u3000'};
        ByteBuffer bb = ByteBuffer.wrap(ba);
        CharBuffer cb = cs.decode(bb);
        if(!Arrays.equals(ca, Arrays.copyOf(cb.array(), cb.limit()))) {
            throw new Exception("IBM964 failed to decode");
        }
        cb = CharBuffer.wrap(ca);
        bb = cs.encode(cb);
        if(!Arrays.equals(ba, Arrays.copyOf(bb.array(), bb.limit()))) {
            throw new Exception("IBM964 failed to encode");
        }
    }

    public static void main (String[] args) throws Exception {
        bug6577466();
        
        bug6639450();
        bug6371437();
        bug6371422();
        bug6371416();
        bug6371619();
        bug6371431();
        bug6569191();
        bug8202329();
        bug8212794();
        bug8213618();
    }
}
