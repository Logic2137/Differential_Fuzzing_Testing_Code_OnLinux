public class TestEliminateAllocationPhi {

    static Integer m2(Integer I, int i) {
        for (; i < 10; i = (i + 2) * (i + 2)) {
        }
        if (i == 121) {
            return II;
        }
        return I;
    }

    static Integer II = new Integer(42);

    static int m(int[] integers, boolean flag) {
        int j = 0;
        while (true) {
            try {
                int k = integers[j++];
                if (flag) {
                    k += 42;
                }
                if (k < 1000) {
                    throw new Exception();
                }
                Integer I = new Integer(k);
                I = m2(I, 0);
                int i = I.intValue();
                return i;
            } catch (Exception e) {
            }
        }
    }

    static public void main(String[] args) {
        for (int i = 0; i < 5000; i++) {
            m2(null, 1);
        }
        int[] integers = { 2000 };
        for (int i = 0; i < 6000; i++) {
            m(integers, (i % 2) == 0);
        }
        int[] integers2 = { 1, 2, 3, 4, 5, 2000 };
        for (int i = 0; i < 10000; i++) {
            m(integers2, (i % 2) == 0);
        }
    }
}
