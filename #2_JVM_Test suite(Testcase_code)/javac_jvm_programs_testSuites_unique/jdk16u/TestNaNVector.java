



package compiler.vectorization;

public class TestNaNVector {
    private char[] array;
    private static final int LEN = 1024;

    public static void main(String args[]) {
        TestNaNVector test = new TestNaNVector();
        
        for (int i = 0; i < 10_000; ++i) {
          test.vectorizeNaNDP();
        }
        System.out.println("Checking double precision Nan");
        test.checkResult(0xfff7);

        
        for (int i = 0; i < 10_000; ++i) {
          test.vectorizeNaNSP();
        }
        System.out.println("Checking single precision Nan");
        test.checkResult(0xff80);
    }

    public TestNaNVector() {
        array = new char[LEN];
    }

    public void vectorizeNaNDP() {
        
        
        
        
        
        
        
        for (int i = 0; i < LEN; ++i) {
            array[i] = 0xfff7;
        }
    }

    public void vectorizeNaNSP() {
        
        for (int i = 0; i < LEN; ++i) {
            array[i] = 0xff80;
        }
    }

    public void checkResult(int expected) {
        for (int i = 0; i < LEN; ++i) {
            if (array[i] != expected) {
                throw new RuntimeException("Invalid result: array[" + i + "] = " + (int)array[i] + " != " + expected);
            }
        }
    }
}

