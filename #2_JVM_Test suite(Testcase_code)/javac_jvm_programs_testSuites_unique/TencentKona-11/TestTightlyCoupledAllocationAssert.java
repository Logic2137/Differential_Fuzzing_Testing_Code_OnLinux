



package compiler.arraycopy;

public class TestTightlyCoupledAllocationAssert {

    static A a;
    static byte[] bArr = new byte[8];

    public static void main(String[] strArr) {
        for (int i = 0; i < 10000; i++ ) {
            test(i % 20);
        }
    }

    public static void test(int i) {
        byte [] bArrLocal = new byte[8]; 
        if (i < 16) {
            a = new A(); 
            return;
        }
        
        
        
        System.arraycopy(bArr, 0, bArrLocal, 0, 8);
    }
}

class A { }
