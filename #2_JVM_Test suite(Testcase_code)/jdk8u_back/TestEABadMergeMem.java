public class TestEABadMergeMem {

    static class Box {

        int i;
    }

    static void m_notinlined() {
    }

    static float dummy1;

    static float dummy2;

    static int test(Box a, Box c, int i, int j, int k, boolean flag1, boolean flag2) {
        Box b = new Box();
        a.i = i;
        b.i = j;
        c.i = k;
        m_notinlined();
        boolean flag3 = false;
        if (flag1) {
            for (int ii = 0; ii < 100; ii++) {
                if (flag2) {
                    dummy1 = (float) ii;
                } else {
                    dummy2 = (float) ii;
                }
            }
            flag3 = true;
        }
        if (flag3) {
            int res = c.i + b.i;
            m_notinlined();
            return res;
        } else {
            return 44 + 43;
        }
    }

    static public void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            Box a = new Box();
            Box c = new Box();
            int res = test(a, c, 42, 43, 44, (i % 2) == 0, (i % 3) == 0);
            if (res != 44 + 43) {
                throw new RuntimeException("Bad result " + res);
            }
        }
    }
}
