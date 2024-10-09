
package compiler.c2.loopopts;

public class TestSplitThruPhiDivMod {

    int x;

    public int testMod() {
        int i1 = 2;
        for (int i = 5; i < 25; i++) {
            for (int j = 50; j > 1; j -= 2) {
                x = (20 % j);
                i1 = (i1 / i);
                for (int k = 3; k > 1; k--) {
                    switch((i % 4) + 22) {
                        case 22:
                            switch(j % 10) {
                                case 83:
                                    x += 5;
                                    break;
                            }
                    }
                }
            }
        }
        return i1;
    }

    public int testDiv() {
        int i1 = 2;
        for (int i = 5; i < 25; i++) {
            for (int j = 50; j > 1; j -= 2) {
                x = (20 / j);
                i1 = (i1 / i);
                for (int k = 3; k > 1; k--) {
                    switch((i % 4) + 22) {
                        case 22:
                            switch(j % 10) {
                                case 83:
                                    x += 5;
                                    break;
                            }
                    }
                }
            }
        }
        return i1;
    }

    public static void main(String[] strArr) {
        TestSplitThruPhiDivMod t = new TestSplitThruPhiDivMod();
        for (int i = 0; i < 10000; i++) {
            t.testDiv();
            t.testMod();
        }
    }
}
