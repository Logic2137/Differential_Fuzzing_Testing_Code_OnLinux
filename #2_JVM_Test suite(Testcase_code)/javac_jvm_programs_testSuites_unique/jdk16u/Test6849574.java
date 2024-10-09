



package compiler.c1;

import java.util.concurrent.atomic.AtomicReferenceArray;

public class Test6849574 extends Thread {

    public static void main(String[] args) {
        AtomicReferenceArray a = new AtomicReferenceArray(10000);
        for (int i = 0; i < 100000; i++) {
            a.getAndSet(9999, new Object());
            if (i > 99990) System.gc();
        }
    }
}
