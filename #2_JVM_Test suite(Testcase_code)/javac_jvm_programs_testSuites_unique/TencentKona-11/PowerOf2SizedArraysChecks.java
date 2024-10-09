



package compiler.rangechecks;

import java.util.function.BiFunction;
import java.util.function.Function;

public class PowerOf2SizedArraysChecks {

    static void check_result(String name, int x, int m, boolean expected, boolean res) {
        if (expected != res) {
            throw new RuntimeException("Bad result in " + name + " for x =  " + x + " m = " + m + " expected " + expected  + " got " + res);
        }
    }

    static void helper(String name, BiFunction<Integer, int[], Boolean> test, int[] x_values, int[] m_values, boolean[][] expected) {
        for (int i = 0; i < x_values.length; i++) {
            int x = x_values[i];
            for (int j = 0; j < m_values.length; j++) {
                int m = m_values[j];
                int[] array = new int[m];
                boolean res = test.apply(x, array);
                check_result(name, x, m, expected[i][j], res);
            }
        }
    }

    static void check_result(String name, int m, boolean expected, boolean res) {
        if (expected != res) {
            throw new RuntimeException("Bad result in " + name + " for m = " + m + " expected " + expected  + " got " + res);
        }
    }

    static void helper2(String name, Function<int[], Boolean> test, int[] m_values, boolean[] expected) {
        for (int j = 0; j < m_values.length; j++) {
            int m = m_values[j];
            int[] array = new int[m];
            boolean res = test.apply(array);
            check_result(name, m, expected[j], res);
        }
    }

    
    static boolean test1(int x, int[] array) {
        int m = array.length;
        if ((x & m) < 0 || (x & m) > m) {
            return false;
        }
        return true;
    }

    
    static boolean test2(int x, int[] array) {
        int m = array.length;
        if ((x & (m-1)) < 0 || (x & (m-1)) >= m) {
            return false;
        }
        return true;
    }

    static boolean test3(int x, int[] array) {
        try {
            int v = array[x & (array.length-1)];
        } catch(ArrayIndexOutOfBoundsException aioobe) {
            return false;
        }
        return true;
    }

    
    static boolean test4(int[] array) {
        if (array.length <= 0) {
            return false;
        }
        return true;
    }

    
    static boolean test5(int[] array) {
        if (array.length == 0) {
            return false;
        }
        return true;
    }

    
    static boolean test6(int[] array) {
        if (array.length != 0) {
            return false;
        }
        return true;
    }

    static public void main(String[] args) {
        int[] x_values = {-10, -5, 0, 5, 8, 16, 100};
        int[] m_values = { 16, 10, 0 };

        boolean[][] test1_expected = new boolean[x_values.length][m_values.length];
        for (int i = 0; i < x_values.length; i++) {
            for (int j = 0; j < m_values.length; j++) {
                test1_expected[i][j] = true;
            }
        }

        boolean[][] test2_expected = new boolean[x_values.length][m_values.length];
        for (int i = 0; i < x_values.length; i++) {
            for (int j = 0; j < m_values.length; j++) {
                test2_expected[i][j] = (m_values[j] > 0);
            }
        }

        boolean[] test4_expected = new boolean[m_values.length];
        for (int i = 0; i < m_values.length; i++) {
            test4_expected[i] = (m_values[i] > 0);
        }
        boolean[] test5_expected = new boolean[m_values.length];
        for (int i = 0; i < m_values.length; i++) {
            test5_expected[i] = (m_values[i] != 0);
        }
        boolean[] test6_expected = new boolean[m_values.length];
        for (int i = 0; i < m_values.length; i++) {
            test6_expected[i] = (m_values[i] == 0);
        }

        for (int i = 0; i < 20000; i++) {
            helper("test1", PowerOf2SizedArraysChecks::test1, x_values, m_values, test1_expected);
            helper("test2", PowerOf2SizedArraysChecks::test2, x_values, m_values, test2_expected);
            helper("test3", PowerOf2SizedArraysChecks::test3, x_values, m_values, test2_expected);
            helper2("test4", PowerOf2SizedArraysChecks::test4, m_values, test4_expected);
            helper2("test5", PowerOf2SizedArraysChecks::test5, m_values, test5_expected);
            helper2("test6", PowerOf2SizedArraysChecks::test6, m_values, test6_expected);
        }
    }
}
