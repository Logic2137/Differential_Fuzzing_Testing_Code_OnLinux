



package compiler.c2;

public class Test6711100 {

    static byte b;

    
    
    public Test6711100() {
        b = (new byte[1])[(new byte[f()])[-1]];
    }

    protected static int f() {
      return 1;
    }

    public static void main(String[] args) {
      try {
        Test6711100 t = new Test6711100();
      } catch (ArrayIndexOutOfBoundsException e) {
      }
    }
}


