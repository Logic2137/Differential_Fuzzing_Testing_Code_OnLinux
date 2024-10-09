



package compiler.arguments;

public class TestStressReflectiveCode {

    public static void main(String[] args) {
        VALUES.clone();
        VALUES2.clone();
    }

    public static class Payload implements Cloneable {
        int i1;
        int i2;
        int i3;
        int i4;

        public Object clone() {
          try {
            return super.clone();
          } catch (CloneNotSupportedException e) {
          }
          return null;
        }
    }

    private static final int[]   VALUES = new int[]{3, 4, 5};
    private static final Payload VALUES2 = new Payload();
}

