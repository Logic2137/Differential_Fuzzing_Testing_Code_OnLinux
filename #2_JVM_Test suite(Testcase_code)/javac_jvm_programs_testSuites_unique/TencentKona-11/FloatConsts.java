


public final class FloatConsts {
    
    private FloatConsts() {}

    
    public static final int     EXP_BIAS        = 127;

    
    public static final int     EXP_BIT_MASK    = 0x7F800000;

    
    public static final int     SIGN_BIT_MASK   = 0x80000000;

    
    public static final int     SIGNIF_BIT_MASK = 0x007FFFFF;

    
    public static final int SIGNIFICAND_WIDTH   = 24;

    
    public static final int     MIN_SUB_EXPONENT = Float.MIN_EXPONENT -
                                                   (SIGNIFICAND_WIDTH - 1);

    static {
        
        
        assert(((SIGN_BIT_MASK | EXP_BIT_MASK | SIGNIF_BIT_MASK) == ~0) &&
               (((SIGN_BIT_MASK & EXP_BIT_MASK) == 0) &&
                ((SIGN_BIT_MASK & SIGNIF_BIT_MASK) == 0) &&
                ((EXP_BIT_MASK & SIGNIF_BIT_MASK) == 0)));
    }
}
