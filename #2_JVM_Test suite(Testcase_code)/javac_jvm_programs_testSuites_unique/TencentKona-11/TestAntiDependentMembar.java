



package compiler.controldependency;

public class TestAntiDependentMembar {

    static volatile int step1 = 0;
    static volatile int step2 = 0;

    public static int test1(int count, int b1, int b2) {
        int[] result = {0};

        
        if (b1 == 0) {
            count += 1;
        } else if (b1 == 1) {
            if (b2 == 1) {
                count += 2;
            }
        }

        for (int i = 0; i < count; ++i) {
            
            step1 = i;
            
            result[0] += count;
        }
        return result[0];
    }

    
    public static int test2(int count) {
        int[] result = {0};

        
        hitSearchLimit();

        for (int i = 0; i < count; ++i) {
            step1 = i;
            result[0] += count;
        }
        return result[0];
    }

    
    public static int test3(int count) {
        int[] result = {0};

        hitSearchLimit();

        for (int i = 0; i < count; ++i) {
            step1 = i;
            step2 = i;
            step1 = i;
            step2 = i;
            result[0] += count;
        }
        return result[0];
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50_000; ++i) {
          test1(10, 0, 0);
          test1(10, 1, 1);
          test1(10, 1, 0);
          test1(10, 0, 1);
          test2(10);
          test3(10);
        }
    }

    public static void hitSearchLimit() {
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
        step1++;
        step2++;
    }
}
