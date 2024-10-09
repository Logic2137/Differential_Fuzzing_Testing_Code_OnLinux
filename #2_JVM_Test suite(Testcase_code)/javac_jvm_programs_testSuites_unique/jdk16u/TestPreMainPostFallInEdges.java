



package compiler.loopopts;

public class TestPreMainPostFallInEdges {

    public static int test() {
        int iArr[] = new int[400];
        float fArr[] = new float[400];
        int x = 0;
        byte y = 124;
        short z = 0;

        int i = 1;
        do {
            int j = 1;
            do {
                z *= 11;

                
                
                
                
                iArr[j] = 3;
                
                iArr[j + 1] += 4;
                fArr[j] = 5;
                fArr[j + 1] += fArr[j + 5]; 

                int k = 1;
                do {
                    iArr[j] *= 324;
                    x = 34;
                    y *= 54;
                } while (k < 1);
            } while (++j < 6);
        } while (++i < 289);
        return checkSum(iArr) + checkSum(fArr);
    }

    public static int checkSum(int[] a) {
        int sum = 0;
        for (int j = 0; j < a.length; j++) {
            sum += a[j] % (j + 1);
        }
        return sum;
    }

    public static int checkSum(float[] a) {
        int sum = 0;
        for (int j = 0; j < a.length; j++) {
            sum += a[j] % (j + 1);
        }
        return sum;
    }

    public static void main(String[] strArr) {
        for (int i = 0; i < 10000; i++) {
            test();
        }
    }
}

