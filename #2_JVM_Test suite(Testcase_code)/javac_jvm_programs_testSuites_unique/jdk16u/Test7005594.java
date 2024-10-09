



package compiler.c2;

public class Test7005594 {
    static int test(byte a[]){
        int result = 0;
        for (int i = 1; i < a.length; i += Integer.MAX_VALUE) {
            result += a[i];
        }
        return result;
    }

    public static void main(String [] args){
        try {
            int result = test(new byte[2]);
            throw new AssertionError("Expected ArrayIndexOutOfBoundsException was not thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Expected " + e + " was thrown");
        }
    }
}
