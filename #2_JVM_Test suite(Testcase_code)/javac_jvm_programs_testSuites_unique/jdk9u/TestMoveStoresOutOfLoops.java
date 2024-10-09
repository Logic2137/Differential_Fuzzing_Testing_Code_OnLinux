



package compiler.loopopts;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.function.Function;

public class TestMoveStoresOutOfLoops {

    private static long[] array = new long[10];
    private static long[] array2 = new long[10];
    private static boolean[] array3 = new boolean[1000];
    private static byte[] byte_array = new byte[10];

    
    
    static void test_after_1(int idx) {
        for (int i = 0; i < 1000; i++) {
            array[idx] = i;
        }
    }

    
    
    static void test_after_2(int idx) {
        for (int i = 0; i < 1000; i++) {
            array[idx] = i;
            array2[i%10] = i;
        }
    }

    
    
    static void test_after_3(int idx) {
        for (int i = 0; i < 1000; i++) {
            array[idx] = i;
            if (array[0] == -1) {
                break;
            }
        }
    }

    
    
    static void test_after_4(int idx) {
        for (int i = 0; i < 1000; i++) {
            if (array[0] == -2) {
                break;
            }
            array[idx] = i;
        }
    }

    
    
    static void test_after_5(int idx) {
        for (int i = 0; i < 1000; i++) {
            array[idx] = i;
            array[idx+1] = i;
            array[idx+2] = i;
            array[idx+3] = i;
            array[idx+4] = i;
            array[idx+5] = i;
        }
    }

    
    
    static void test_after_6(int idx) {
        for (int i = 0; i < 1000; i++) {
            array[idx] = i;
            if (array3[i]) {
                return;
            }
        }
    }

    
    static void test_stores_1(int ignored) {
        array[0] = 0;
        array[1] = 1;
        array[2] = 2;
        array[0] = 0;
        array[1] = 1;
        array[2] = 2;
    }

    static void test_stores_2(int idx) {
        array[idx+0] = 0;
        array[idx+1] = 1;
        array[idx+2] = 2;
        array[idx+0] = 0;
        array[idx+1] = 1;
        array[idx+2] = 2;
    }

    static void test_stores_3(int idx) {
        byte_array[idx+0] = 0;
        byte_array[idx+1] = 1;
        byte_array[idx+2] = 2;
        byte_array[idx+0] = 0;
        byte_array[idx+1] = 1;
        byte_array[idx+2] = 2;
    }

    
    static void test_before_1(int idx) {
        for (int i = 0; i < 1000; i++) {
            array[idx] = 999;
        }
    }

    
    
    static void test_before_2(int idx) {
        for (int i = 0; i < 1000; i++) {
            array[idx] = 999;
            array[i%2] = 0;
        }
    }

    
    
    static int test_before_3(int idx) {
        int res = 0;
        for (int i = 0; i < 1000; i++) {
            res += array[i%10];
            array[idx] = 999;
        }
        return res;
    }

    
    
    static void test_before_4(int idx) {
        for (int i = 0; i < 1000; i++) {
            if (idx / (i+1) > 0) {
                return;
            }
            array[idx] = 999;
        }
    }

    
    
    static void test_before_5(int idx) {
        for (int i = 0; i < 1000; i++) {
            if (i % 2 == 0) {
                array[idx] = 999;
            }
        }
    }

    
    static int test_before_6(int idx) {
        int res = 0;
        for (int i = 0; i < 1000; i++) {
            if (i%2 == 1) {
                res *= 2;
            } else {
                res++;
            }
            array[idx] = 999;
        }
        return res;
    }

    final HashMap<String,Method> tests = new HashMap<>();
    {
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.getName().matches("test_(before|after|stores)_[0-9]+")) {
                assert(Modifier.isStatic(m.getModifiers())) : m;
                tests.put(m.getName(), m);
            }
        }
    }

    boolean success = true;
    void doTest(String name, Runnable init, Function<String, Boolean> check) throws Exception {
        Method m = tests.get(name);
        for (int i = 0; i < 20000; i++) {
            init.run();
            m.invoke(null, 0);
            success = success && check.apply(name);
            if (!success) {
                break;
            }
        }
    }

    static void array_init() {
        array[0] = -1;
    }

    static boolean array_check(String name) {
        boolean success = true;
        if (array[0] != 999) {
            success = false;
            System.out.println(name + " failed: array[0] = " + array[0]);
        }
        return success;
    }

    static void array_init2() {
        for (int i = 0; i < 6; i++) {
            array[i] = -1;
        }
    }

    static boolean array_check2(String name) {
        boolean success = true;
        for (int i = 0; i < 6; i++) {
            if (array[i] != 999) {
                success = false;
                System.out.println(name + " failed: array[" + i + "] = " + array[i]);
            }
        }
        return success;
    }

    static void array_init3() {
        for (int i = 0; i < 3; i++) {
            array[i] = -1;
        }
    }

    static boolean array_check3(String name) {
        boolean success = true;
        for (int i = 0; i < 3; i++) {
            if (array[i] != i) {
                success = false;
                System.out.println(name + " failed: array[" + i + "] = " + array[i]);
            }
        }
        return success;
    }

    static void array_init4() {
        for (int i = 0; i < 3; i++) {
            byte_array[i] = -1;
        }
    }

    static boolean array_check4(String name) {
        boolean success = true;
        for (int i = 0; i < 3; i++) {
            if (byte_array[i] != i) {
                success = false;
                System.out.println(name + " failed: byte_array[" + i + "] = " + byte_array[i]);
            }
        }
        return success;
    }

    static public void main(String[] args) throws Exception {
        TestMoveStoresOutOfLoops test = new TestMoveStoresOutOfLoops();
        test.doTest("test_after_1", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);
        test.doTest("test_after_2", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);
        test.doTest("test_after_3", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);
        test.doTest("test_after_4", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);
        test.doTest("test_after_5", TestMoveStoresOutOfLoops::array_init2, TestMoveStoresOutOfLoops::array_check2);
        test.doTest("test_after_6", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);
        array3[999] = true;
        test.doTest("test_after_6", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);

        test.doTest("test_stores_1", TestMoveStoresOutOfLoops::array_init3, TestMoveStoresOutOfLoops::array_check3);
        test.doTest("test_stores_2", TestMoveStoresOutOfLoops::array_init3, TestMoveStoresOutOfLoops::array_check3);
        test.doTest("test_stores_3", TestMoveStoresOutOfLoops::array_init4, TestMoveStoresOutOfLoops::array_check4);

        test.doTest("test_before_1", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);
        test.doTest("test_before_2", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);
        test.doTest("test_before_3", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);
        test.doTest("test_before_4", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);
        test.doTest("test_before_5", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);
        test.doTest("test_before_6", TestMoveStoresOutOfLoops::array_init, TestMoveStoresOutOfLoops::array_check);

        if (!test.success) {
            throw new RuntimeException("Some tests failed");
        }
    }
}
