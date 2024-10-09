



package compiler.arraycopy;
import java.util.Arrays;

class test {
    public static int exp_count = 0;
    public int in1 = -4096;
    test (){
        try {
            short sha4[] = new short[1012];
            for (int i = 0; i < sha4.length; i++) {
              sha4[i] = 9;
            }
            Arrays.copyOf(sha4, in1);
        } catch (Exception ex) {
            exp_count++;
        }
    }
}

public class TestNegativeArrayCopyAfterLoop {
    public static void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            new test();
        }
        if (test.exp_count == 20000) {
            System.out.println("TEST PASSED");
        }
    }
}
