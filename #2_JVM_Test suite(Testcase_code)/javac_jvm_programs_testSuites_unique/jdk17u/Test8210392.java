
package compiler.loopopts;

public class Test8210392 {

    public static int ival = 17;

    public static int intFn() {
        int v = 0, k = 0;
        for (int i = 17; i < 311; i += 3) {
            v = Test8210392.ival;
            int j = 1;
            do {
                v *= i;
                v += j * v;
                while (++k < 1) ;
            } while (++j < 13);
        }
        return v;
    }

    public void mainTest() {
        for (int i = 0; i < 30000; i++) {
            Test8210392.ival = intFn();
        }
    }

    public static void main(String[] _args) {
        Test8210392 tc = new Test8210392();
        for (int i = 0; i < 10; i++) {
            tc.mainTest();
        }
    }
}
