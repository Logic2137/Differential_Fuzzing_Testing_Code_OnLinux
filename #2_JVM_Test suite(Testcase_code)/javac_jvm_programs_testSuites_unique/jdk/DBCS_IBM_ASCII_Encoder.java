




import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import sun.nio.cs.Surrogate;



abstract class DBCS_IBM_ASCII_Encoder extends CharsetEncoder
{

    protected static final char REPLACE_CHAR='\uFFFD';
    private byte b1;
    private byte b2;


    protected short index1[];
    protected String index2;
    protected String index2a;
    protected int   mask1;
    protected int   mask2;
    protected int   shift;

    private final Surrogate.Parser sgp = new Surrogate.Parser();

    protected DBCS_IBM_ASCII_Encoder(Charset cs) {
        super(cs, 2.0f, 2.0f);
    }

    
    public boolean canEncode(char ch) {
       int  index;
       int  theBytes;

       index = index1[((ch & mask1) >> shift)] + (ch & mask2);
       if (index < 15000)
         theBytes = (int)(index2.charAt(index));
       else
         theBytes = (int)(index2a.charAt(index-15000));

       if (theBytes != 0)
         return (true);

       
       
       return( ch == '\u0000');

    }

    private CoderResult encodeArrayLoop(CharBuffer src, ByteBuffer dst) {
        char[] sa = src.array();
        int sp = src.arrayOffset() + src.position();
        int sl = src.arrayOffset() + src.limit();
        byte[] da = dst.array();
        int dp = dst.arrayOffset() + dst.position();
        int dl = dst.arrayOffset() + dst.limit();
        int outputSize = 0;             

        try {
            while (sp < sl) {
                int index;
                int theBytes;
                char c = sa[sp];
                if (Surrogate.is(c)) {
                    if (sgp.parse(c, sa, sp, sl) < 0)
                        return sgp.error();
                    return sgp.unmappableResult();
                }
                if (c >= '\uFFFE')
                    return CoderResult.unmappableForLength(1);

                index = index1[((c & mask1) >> shift)]
                                + (c & mask2);
                if (index < 15000)
                    theBytes = (int)(index2.charAt(index));
                else
                    theBytes = (int)(index2a.charAt(index-15000));
                b1 = (byte)((theBytes & 0x0000ff00)>>8);
                b2 = (byte)(theBytes & 0x000000ff);

                if (b1 == 0x00 && b2 == 0x00
                    && c != '\u0000') {
                        return CoderResult.unmappableForLength(1);
                }

                if (b1 == 0) {
                    if (dl - dp < 1)
                        return CoderResult.OVERFLOW;
                    da[dp++] = (byte) b2;
                } else {
                    if (dl - dp < 2)
                        return CoderResult.OVERFLOW;
                    da[dp++] = (byte) b1;
                    da[dp++] = (byte) b2;
                }
                sp++;
            }
            return CoderResult.UNDERFLOW;
        } finally {
            src.position(sp - src.arrayOffset());
            dst.position(dp - dst.arrayOffset());
        }
    }

    private CoderResult encodeBufferLoop(CharBuffer src, ByteBuffer dst) {
        int mark = src.position();
        int outputSize = 0;             

        try {
            while (src.hasRemaining()) {
                int index;
                int theBytes;
                char c = src.get();
                if (Surrogate.is(c)) {
                    if (sgp.parse(c, src) < 0)
                        return sgp.error();
                    return sgp.unmappableResult();
                }
                if (c >= '\uFFFE')
                    return CoderResult.unmappableForLength(1);

                index = index1[((c & mask1) >> shift)]
                                + (c & mask2);
                if (index < 15000)
                    theBytes = (int)(index2.charAt(index));
                else
                    theBytes = (int)(index2a.charAt(index-15000));
                b1 = (byte)((theBytes & 0x0000ff00)>>8);
                b2 = (byte)(theBytes & 0x000000ff);

                if (b1 == 0x00 && b2 == 0x00
                    && c != '\u0000') {
                        return CoderResult.unmappableForLength(1);
                }

                if (b1 == 0) {
                    if (dst.remaining() < 1)
                        return CoderResult.OVERFLOW;
                    dst.put((byte) b2);
                } else {
                    if (dst.remaining() < 2)
                        return CoderResult.OVERFLOW;
                    dst.put((byte) b1);
                    dst.put((byte) b2);
                }
                mark++;
             }
            return CoderResult.UNDERFLOW;
        } finally {
            src.position(mark);
        }
    }

    protected CoderResult encodeLoop(CharBuffer src, ByteBuffer dst) {
        if (true && src.hasArray() && dst.hasArray())
            return encodeArrayLoop(src, dst);
        else
            return encodeBufferLoop(src, dst);
    }

}
