



package compiler.c2;

public class TestDominatingDeadCheckCast {

    static class A {
        int f;
    }

    static class B extends A {
    }

    static A not_inlined() {
        return new A();
    }

    static void inlined(A param) {
        param.f = 42;
    }

    static A field;

    static void test(boolean flag1, boolean flag2, boolean flag3, boolean flag4, boolean flag5) {
        
        field = not_inlined();
        
        
        inlined(field);
        
        
        if (flag1) {
            if (flag2) {
                if (flag3) {
                    
                    
                    
                    
                    
                    inlined(field);
                    
                    if (flag4) {
                        if (flag5) {
                            
                            
                            
                            
                            
                            inlined(field);
                        }
                    }
                }
            }
        }
    }

    static public void main(String[] args) {
        field = new A();
        for (int i = 0; i < 20000; i++) {
            test(true, true, true, true, true);
        }
    }
}
