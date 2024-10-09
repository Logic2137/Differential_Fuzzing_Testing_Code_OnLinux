



package compiler.vectorization;

import java.util.stream.IntStream;

public class TestForEachRem {
    static final int RANGE = 512;
    static final int ITER  = 100;

    static void test1(int[] data) {
       IntStream.range(0, RANGE).parallel().forEach(j -> {
           data[j] = j + 1;
       });
    }

    static void test2(int[] data) {
       IntStream.range(0, RANGE - 1).forEach(j -> {
           data[j] = data[j] + data[j + 1];
       });
    }

    static void test3(int[] data, int A, int B) {
       IntStream.range(0, RANGE - 1).forEach(j -> {
           data[j] = A * data[j] + B * data[j + 1];
       });
    }

    static void test4(int[] data) {
       IntStream.range(0, RANGE - 1).forEach(j -> {
           data[j + 1] = data[j];
       });
    }

    static void verify(String name, int[] data, int[] gold) {
        for (int i = 0; i < RANGE; i++) {
            if (data[i] != gold[i]) {
                throw new RuntimeException(" Invalid " + name + " result: data[" + i + "]: " + data[i] + " != " + gold[i]);
            }
        }
    }

    public static void main(String[] args) {
        int[] data = new int[RANGE];
        int[] gold = new int[RANGE];

        if (args.length == 0) {
            throw new RuntimeException(" Missing test name: test1, test2, test3, test4");
        }

        if (args[0].equals("test1")) {
            System.out.println(" Run test1 ...");
            test1(gold);
            for (int i = 0; i < ITER; i++) {
                test1(data);
            }
            verify("test1", data, gold);
            System.out.println(" Finished test1.");
        }

        if (args[0].equals("test2")) {
            System.out.println(" Run test2 ...");
            test1(gold);
            test2(gold);
            for (int i = 0; i < ITER; i++) {
                test1(data); 
                test2(data);
            }
            verify("test2", data, gold);
            System.out.println(" Finished test2.");
        }

        if (args[0].equals("test3")) {
            System.out.println(" Run test3 ...");
            test1(gold);
            test3(gold, 2, 3);
            for (int i = 0; i < ITER; i++) {
                test1(data); 
                test3(data, 2, 3);
            }
            verify("test3", data, gold);
            System.out.println(" Finished test3.");
        }

        if (args[0].equals("test4")) {
            System.out.println(" Run test4 ...");
            test1(gold); 
            test4(gold);
            for (int i = 0; i < ITER; i++) {
                test1(data); 
                test4(data);
            }
            verify("test4", data, gold);
            System.out.println(" Finished test4.");
        }
    }
}
