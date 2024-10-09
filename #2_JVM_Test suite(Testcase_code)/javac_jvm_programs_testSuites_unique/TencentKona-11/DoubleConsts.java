


public final class DoubleConsts {
    
    private DoubleConsts() {}

    
    public static final int     EXP_BIAS        = 1023;

    
    public static final long    EXP_BIT_MASK    = 0x7FF0000000000000L;

    
    public static final long    SIGN_BIT_MASK   = 0x8000000000000000L;

    
    public static final long    SIGNIF_BIT_MASK = 0x000FFFFFFFFFFFFFL;

    
    public static final int SIGNIFICAND_WIDTH   = 53;

    
    public static final int     MIN_SUB_EXPONENT = Double.MIN_EXPONENT -
                                                   (SIGNIFICAND_WIDTH - 1);

    static {
        
        
        assert(((SIGN_BIT_MASK | EXP_BIT_MASK | SIGNIF_BIT_MASK) == ~0L) &&
               (((SIGN_BIT_MASK & EXP_BIT_MASK) == 0L) &&
                ((SIGN_BIT_MASK & SIGNIF_BIT_MASK) == 0L) &&
                ((EXP_BIT_MASK & SIGNIF_BIT_MASK) == 0L)));
    }
}
