



package compiler.c2;

public class Test6799693 {
    static int var_bad = 1;

    public static void main(String[] args) {
        var_bad++;

        try {
            for (int i = 0; i < 10; i++) (new byte[((byte) -1 << i)])[0] = 0;
        } catch (Exception e) {
            System.out.println("Got " + e);
        }

        System.out.println("Test.var_bad = " + var_bad + " (expected 2)\n");
    }
}

