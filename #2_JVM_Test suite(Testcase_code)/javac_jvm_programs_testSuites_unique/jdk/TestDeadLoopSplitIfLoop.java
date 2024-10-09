


package compiler.c2;

public class TestDeadLoopSplitIfLoop {
    int a;
    int b;
    boolean c;

    public static void main(String[] g) {
        TestDeadLoopSplitIfLoop h = new TestDeadLoopSplitIfLoop();
        h.test();
    }

    void test() {
        int e = 4;
        long f[] = new long[a];
        if (c) {
        } else if (c) {
            
            switch (126) {
                case 126:
                    do {
                        f[e] = b;
                        switch (6) {
                            case 7:
                                f = f;
                        }
                    } while (e++ < 93);
            }
        }
    }
}
