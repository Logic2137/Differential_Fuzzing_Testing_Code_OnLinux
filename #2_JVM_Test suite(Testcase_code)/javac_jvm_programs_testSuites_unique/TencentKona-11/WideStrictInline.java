



import java.io.PrintStream;

public class WideStrictInline {
    static PrintStream out;
    static float halfUlp;

    static {
        halfUlp = 1;
        for ( int i = 127 - 24; i > 0; i-- )
            halfUlp *= 2;
    }

    public static void main(String argv[]) throws Exception {
        out = System.err;
        pr(-1,"halfUlp",halfUlp);
        WideStrictInline obj = new WideStrictInline();
        for( int i=0; i<48; i++ )
            obj.instanceMethod( i );
    }

    private static void pr(int i, String desc, float r) {
        out.print(" i=("+i+") "+desc+" ; == "+r);
        out.println(" , 0x"+Integer.toHexString(Float.floatToIntBits(r)));
    }

    private static strictfp float WideStrictInline(float par) {
        return par;
    }

    public static strictfp float strictValue(int i) {
        float r;
        switch (i%4) {
        case 0: r = -Float.MAX_VALUE;  break;
        case 1: r =  Float.MAX_VALUE;  break;
        case 2: r =  Float.MIN_VALUE;  break;
        default : r = 1L << 24;
        }
        return r;
    }

    void instanceMethod (int i) throws Exception {
        float r;
        switch (i%4) {
        case 0:
            if (!Float.isInfinite( WideStrictInline(strictValue(i)*2) +
                                   Float.MAX_VALUE ))
                {
                    pr(i,
                       "WideStrictInline(-Float.MAX_VALUE * 2) " +
                       "!= Float.NEGATIVE_INFINITY"
                       ,WideStrictInline(strictValue(i)*2) + Float.MAX_VALUE);
                }
            r = WideStrictInline(strictValue(i)*2) + Float.MAX_VALUE;
            if ( !Float.isInfinite( r ) ) {
                pr(i,"r != Float.NEGATIVE_INFINITY",r);
                throw new RuntimeException();
            }
            break;
        case 1:
            if (!Float.isInfinite(WideStrictInline(strictValue(i)+halfUlp) -
                                  Float.MAX_VALUE )) {
                pr(i,"WideStrictInline(Float.MAX_VALUE+halfUlp) " +
                   "!= Float.POSITIVE_INFINITY"
                   ,WideStrictInline(strictValue(i)+halfUlp) - Float.MAX_VALUE);
            }
            r = WideStrictInline(strictValue(i)+halfUlp) - Float.MAX_VALUE;
            if ( !Float.isInfinite( r ) ) {
                pr(i,"r != Float.POSITIVE_INFINITY",r);
                throw new RuntimeException();
            }
            break;
        case 2:
            if (WideStrictInline(strictValue(i)/2) != 0) {
                pr(i,"WideStrictInline(Float.MIN_VALUE/2) != 0",
                   WideStrictInline(strictValue(i)/2));
            }
            r = WideStrictInline(strictValue(i)/2);
            if ( r != 0 ) {
                pr(i,"r != 0",r);
                throw new RuntimeException();
            }
            break;
        default:
            if (WideStrictInline(strictValue(i)-0.5f) - strictValue(i) != 0) {
                pr(i,"WideStrictInline(2^24-0.5) != 2^24",
                   WideStrictInline(strictValue(i)-0.5f));
            }
            r = WideStrictInline(strictValue(i)-0.5f);
            if ( r - strictValue(i) != 0 ) {
                pr(i,"r != 2^24",r);
                throw new RuntimeException();
            }
        }
    }

}
