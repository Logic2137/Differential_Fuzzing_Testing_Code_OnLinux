
package compiler.loopopts.superword;

import java.util.Random;

public class Vec_MulAddS2I {

    static final int NUM = 1024;

    static int[] out = new int[NUM];

    static short[] in1 = new short[2 * NUM];

    static short[] in2 = new short[2 * NUM];

    public static void main(String[] args) throws Exception {
        Vec_MulAddS2IInit(in1, in2);
        int result = 0;
        int valid = 204800000;
        for (int j = 0; j < 10000 * 512; j++) {
            result = Vec_MulAddS2IImplement(in1, in2, out);
        }
        if (result == valid) {
            System.out.println("Success");
        } else {
            System.out.println("Invalid calculation of element variables in the out array: " + result);
            System.out.println("Expected value for each element of out array = " + valid);
            throw new Exception("Failed");
        }
    }

    public static void Vec_MulAddS2IInit(short[] in1, short[] in2) {
        for (int i = 0; i < 2 * NUM; i++) {
            in1[i] = (short) 4;
            in2[i] = (short) 5;
        }
    }

    public static int Vec_MulAddS2IImplement(short[] in1, short[] in2, int[] out) {
        for (int i = 0; i < NUM; i++) {
            out[i] += ((in1[2 * i] * in2[2 * i]) + (in1[2 * i + 1] * in2[2 * i + 1]));
        }
        Random rand = new Random();
        int n = rand.nextInt(NUM - 1);
        return out[n];
    }
}
