



package compiler.arraycopy;

import java.util.*;

public class TestArrayCopyMaskedWithZeroSrc {

    public static void main(String[] args) {
        TestArrayCopyMaskedWithZeroSrc t = new TestArrayCopyMaskedWithZeroSrc();
        t.test();
    }

    void test() {
        for (int i = 0; i < 20000; i++) {
            testArrayCopy1(3);
        }
    }

    
    
    

    byte [] testArrayCopy1(int partial_len) {
        byte [] src = new byte[5];
        byte [] dest = Arrays.copyOf(src, partial_len);
        return dest;
    }
}
