



package compiler.escapeAnalysis.cr6795161;

class Test_Class_1 {
    static String var_1;

    static void badFunc(int size)
    {
        try {
          for (int i = 0; i < 1; (new byte[size-i])[0] = 0, i++) {}
        } catch (Exception e) {
          
          
        }
    }
}

public class Test {
    static String var_1_copy = Test_Class_1.var_1;

    static byte var_check;

    public static void main(String[] args)
    {
        var_check = 1;

        Test_Class_1.badFunc(-1);

        System.out.println("EATester.var_check = " + Test.var_check + " (expected 1)\n");
    }
}

