



package compiler.c2;

public class Test6910484 {

    public static void main(String[] args) {
        long iteration = 0;
        for(int i = 0; i <11000; i++) {
            iteration++;
            int result = test(255);
            if (result != 112) {
                System.out.println("expected 112, but got " + result + " after iteration " + iteration);
                System.exit(97);
            }
        }
    }

    private static int test(int x) {
        return (x & -32) / 2;
    }

}
