

package compiler.lib.ir_framework;


public enum Compiler {
    
    ANY(-1),
    
    C1(1),
    
    C2(4),

    ;

    private final int value;

    Compiler(int level) {
        this.value = level;
    }

    
    public int getValue() {
        return value;
    }
}
