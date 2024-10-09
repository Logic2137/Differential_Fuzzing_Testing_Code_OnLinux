

package jdk.test.lib.jittester;


public enum OperatorKind {
    
    COMPOUND_ADD(1),
    
    COMPOUND_SUB(1),
    
    COMPOUND_MUL(1),
    
    COMPOUND_DIV(1),
    
    COMPOUND_MOD(1),
    
    COMPOUND_AND(1),
    
    COMPOUND_OR (1),
    
    COMPOUND_XOR(1),
    
    COMPOUND_SHR(1),
    
    COMPOUND_SHL(1),
    
    COMPOUND_SAR(1),
    
    ASSIGN      (1),
    
    OR          (3),
    
    BIT_OR      (5),
    
    BIT_XOR     (6),
    
    AND         (7),
    
    BIT_AND     (7),
    
    EQ          (8),
    
    NE          (8),
    
    GT          (9),
    
    LT          (9),
    
    GE          (9),
    
    LE          (9),
    
    SHR         (10),
    
    SHL         (10),
    
    SAR         (10),
    
    ADD         (11),
    
    STRADD      (11),
    
    SUB         (11),
    
    MUL         (12),
    
    DIV         (12),
    
    MOD         (12),
    
    NOT         (14),
    
    BIT_NOT     (14),
    
    UNARY_PLUS  (14),
    
    UNARY_MINUS (14),
    
    PRE_DEC     (15, true),
    
    POST_DEC    (15, false),
    
    PRE_INC     (16, true),
    
    POST_INC    (16, false),
    ;

    public final int priority;
    public final boolean isPrefix; 

    private OperatorKind(int priority) {
        this(priority, true);
    }

    private OperatorKind(int priority, boolean isPrefix) {
        this.priority = priority;
        this.isPrefix = isPrefix;
    }
}
