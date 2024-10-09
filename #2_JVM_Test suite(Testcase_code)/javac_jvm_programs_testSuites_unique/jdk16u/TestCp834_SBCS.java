



import java.io.*;
import java.nio.*;
import java.nio.charset.*;

public class TestCp834_SBCS {
    public static void main(String args[]) throws Exception {
        
        
        
        CharsetEncoder enc834 = Charset.forName("Cp834")
                                       .newEncoder()
                                       .onUnmappableCharacter(CodingErrorAction.REPLACE)
                                       .onMalformedInput(CodingErrorAction.REPLACE);

        CharsetDecoder dec834 = Charset.forName("Cp834")
                                       .newDecoder()
                                       .onUnmappableCharacter(CodingErrorAction.REPLACE)
                                       .onMalformedInput(CodingErrorAction.REPLACE);

        CharsetDecoder dec933 = Charset.forName("Cp933")
                                       .newDecoder()
                                       .onUnmappableCharacter(CodingErrorAction.REPLACE)
                                       .onMalformedInput(CodingErrorAction.REPLACE);
        byte[] ba = new byte[1];
        byte[] ba2 = new byte[2];
        ByteBuffer dbb = ByteBuffer.allocateDirect(10);
        char[] ca = new char[1];
        char c;
        for (int i = 0; i <= 0xff; i++) {
            if (i != 0xe && i != 0xf) {   
                ba[0] = (byte)i;
                CharBuffer cb = dec933.decode(ByteBuffer.wrap(ba));
                if ((c = cb.get()) != '\ufffd') {
                    
                    if (dec834.decode(ByteBuffer.wrap(ba)).get() != '\ufffd')
                        throw new RuntimeException("SBCS is supported in IBM834 decoder");

                    if (enc834.canEncode(c))
                        throw new RuntimeException("SBCS can be encoded in IBM834 encoder");

                    ca[0] = c;
                    ByteBuffer bb = enc834.encode(CharBuffer.wrap(ca));
                    if (bb.get() != (byte)0xfe || bb.get() != (byte)0xfe)
                        throw new RuntimeException("SBCS is supported in IBM834 encoder");
                }
            }
        }

        
        if (! new String("\ufffd".getBytes("cp834"), "cp834").equals("\ufffd")) {
            throw new RuntimeException("u+fffd roundtrip failed");
        }

        if (! new String("a".getBytes("cp834"), "cp834").equals("\ufffd") ||
            ! new String(new byte[] { 0x41, 0x40}, "cp834").equals("\ufffd")) {
            throw new RuntimeException("decoding unmappable don't return u+fffd");
        }

        CoderResult cr = Charset.forName("Cp834").newDecoder().decode(
            ByteBuffer.wrap(new byte[] { 0x41, 0x40}), CharBuffer.wrap(new char[2]), true);
        if (cr.isError() && cr.length() != 2) {
            throw new RuntimeException("decoding unmappable don't return unmmappable(2)");
        }

    }
}
