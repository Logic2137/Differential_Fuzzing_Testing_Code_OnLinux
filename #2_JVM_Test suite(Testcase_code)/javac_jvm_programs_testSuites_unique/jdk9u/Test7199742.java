



package compiler.c2;

public class Test7199742 {
    private static final int ITERS = 10000000;

    public static void main(String args[]) {
        Test7199742 t = new Test7199742();
        for (int i = 0; i < 10; i++) {
            test(t, 7);
        }
    }

    static Test7199742 test(Test7199742 t, int m) {
        int i = -(ITERS / 2);
        if (i == 0) return null;
        Test7199742 v = null;
        while (i < ITERS) {
            if ((i & m) == 0) {
                v = t;
            }
            i++;
        }
        return v;
    }
}

