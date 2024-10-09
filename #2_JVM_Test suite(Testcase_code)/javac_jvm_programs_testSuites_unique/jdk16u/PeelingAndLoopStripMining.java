



package compiler.loopopts;

public class PeelingAndLoopStripMining {

    static double dFld = 3.3;

    public void test(int k) {
        for (int i = 1; i < 100; i++) {
            
            if (k == 300) {
                return;
            }

            
            dFld = sqrtAdd(i);
        }
    }

    public static void main(String[] strArr) {
        PeelingAndLoopStripMining _instance = new SubClass();
        for (int i = 0; i < 10000; i++ ) {
            _instance.test(12);
        }

        _instance = new PeelingAndLoopStripMining();
        for (int i = 0; i < 10000; i++ ) {
            _instance.test(300);
        }
        for (int i = 0; i < 10000; i++ ) {
            _instance.test(45);
        }
    }

    public double sqrtAdd(int i) {
        return 1.0 + Math.sqrt(i);
    }
}

class SubClass extends PeelingAndLoopStripMining {
    public double sqrtAdd(int i) {
        return 2.0 + Math.sqrt(i);
    }
}

