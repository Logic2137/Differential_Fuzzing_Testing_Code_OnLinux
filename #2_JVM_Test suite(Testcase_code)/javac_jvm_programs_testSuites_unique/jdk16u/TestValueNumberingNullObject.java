



package compiler.c1;

class T1 {

    public T2 f1;

    public int za() {
        return 0;
    }

    public int zb() {
        return 0;
    }

    public int zc() {
        return 0;
    }

    public int zd() {
        return 0;
    }

    public int ze() {
        return 0;
    }

    public int zf() {
        return 0;
    }

    public int zg() {
        return 0;
    }

    public int zh() {
        return 0;
    }
}

class T2 {

    public T1 f1;

    public int zh() {
        return 0;
    }
}

public class TestValueNumberingNullObject {

    public static void main(String args[]) {
        new T1();  
        new T2();  
        try {
            
            
            
            
            
            testFieldAccess();
        } catch (Exception e) {
        }
        try {
            
            
            
            
            
            
            basicTypeAccess();
        } catch (Exception e) {
        }
    }

    static long testFieldAccess() {
        T1 t1 = null;
        T2 t2 = null;
        T1[] t3 = null;
        T2[] t4 = null;

        long value = t1.f1.zh() + t2.f1.zh();
        
        value += t3[2].f1.zh() + t4[2].f1.zh();
        return value;
    }

    static long basicTypeAccess() {
        long[] f1 = null;
        int[] f2 = null;
        T2[] t2 = null;
        T1[] t1 = null;
        return f1[5] + f2[5] + t2[5].zh() + t1[5].zh();
    }
}

