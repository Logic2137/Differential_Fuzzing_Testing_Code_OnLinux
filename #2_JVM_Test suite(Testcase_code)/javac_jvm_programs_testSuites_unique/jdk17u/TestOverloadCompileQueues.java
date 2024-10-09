
package compiler.classUnloading.methodUnloading;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class TestOverloadCompileQueues {

    public static final int ITERS = 500;

    public static final int ITERS_A = 1000;

    public static int[] iArr = new int[100];

    public static void test0() {
    }

    public static void test1() {
    }

    public static void test2() {
    }

    public static void test3() {
    }

    public static void test4() {
    }

    public static void test5() {
    }

    public static void test6() {
    }

    public static void test7() {
    }

    public static void test8() {
    }

    public static void test9() {
    }

    public static void test10() {
    }

    public static void test11() {
    }

    public static void test12() {
    }

    public static void test13() {
    }

    public static void test14() {
    }

    public static void test15() {
    }

    public static void test16() {
    }

    public static void test17() {
    }

    public static void test18() {
    }

    public static void test19() {
    }

    public static void testA0() {
        Arrays.sort(iArr);
    }

    public static void testA1() {
        Arrays.sort(iArr);
    }

    public static void testA2() {
        Arrays.sort(iArr);
    }

    public static void testA3() {
        Arrays.sort(iArr);
    }

    public static void testA4() {
        Arrays.sort(iArr);
    }

    public static void testA5() {
        Arrays.sort(iArr);
    }

    public static void testA6() {
        Arrays.sort(iArr);
    }

    public static void testA7() {
        Arrays.sort(iArr);
    }

    public static void testA8() {
        Arrays.sort(iArr);
    }

    public static void testA9() {
        Arrays.sort(iArr);
    }

    public static void testA10() {
        Arrays.sort(iArr);
    }

    public static void testA11() {
        Arrays.sort(iArr);
    }

    public static void testA12() {
        Arrays.sort(iArr);
    }

    public static void testA13() {
        Arrays.sort(iArr);
    }

    public static void testA14() {
        Arrays.sort(iArr);
    }

    public static void testA15() {
        Arrays.sort(iArr);
    }

    public static void testA16() {
        Arrays.sort(iArr);
    }

    public static void testA17() {
        Arrays.sort(iArr);
    }

    public static void testA18() {
        Arrays.sort(iArr);
    }

    public static void testA19() {
        Arrays.sort(iArr);
    }

    public static void testA20() {
        Arrays.sort(iArr);
    }

    public static void testA21() {
        Arrays.sort(iArr);
    }

    public static void testA22() {
        Arrays.sort(iArr);
    }

    public static void testA23() {
        Arrays.sort(iArr);
    }

    public static void testA24() {
        Arrays.sort(iArr);
    }

    public static void testA25() {
        Arrays.sort(iArr);
    }

    public static void testA26() {
        Arrays.sort(iArr);
    }

    public static void testA27() {
        Arrays.sort(iArr);
    }

    public static void testA28() {
        Arrays.sort(iArr);
    }

    public static void testA29() {
        Arrays.sort(iArr);
    }

    public static void testA30() {
        Arrays.sort(iArr);
    }

    public static void testA31() {
        Arrays.sort(iArr);
    }

    public static void testA32() {
        Arrays.sort(iArr);
    }

    public static void testA33() {
        Arrays.sort(iArr);
    }

    public static void testA34() {
        Arrays.sort(iArr);
    }

    public static void testA35() {
        Arrays.sort(iArr);
    }

    public static void testA36() {
        Arrays.sort(iArr);
    }

    public static void testA37() {
        Arrays.sort(iArr);
    }

    public static void testA38() {
        Arrays.sort(iArr);
    }

    public static void testA39() {
        Arrays.sort(iArr);
    }

    public static void testA40() {
        Arrays.sort(iArr);
    }

    public static void testA41() {
        Arrays.sort(iArr);
    }

    public static void testA42() {
        Arrays.sort(iArr);
    }

    public static void testA43() {
        Arrays.sort(iArr);
    }

    public static void testA44() {
        Arrays.sort(iArr);
    }

    public static void testA45() {
        Arrays.sort(iArr);
    }

    public static void testA46() {
        Arrays.sort(iArr);
    }

    public static void testA47() {
        Arrays.sort(iArr);
    }

    public static void testA48() {
        Arrays.sort(iArr);
    }

    public static void testA49() {
        Arrays.sort(iArr);
    }

    public static void main(String[] args) throws Throwable {
        run();
        runA();
    }

    public static void run() throws Throwable {
        Class<?> thisClass = TestOverloadCompileQueues.class;
        ClassLoader defaultLoader = thisClass.getClassLoader();
        URL classesDir = thisClass.getProtectionDomain().getCodeSource().getLocation();
        for (int i = 0; i < ITERS; ++i) {
            URLClassLoader myLoader = URLClassLoader.newInstance(new URL[] { classesDir }, defaultLoader.getParent());
            Class<?> testClass = Class.forName(thisClass.getCanonicalName(), true, myLoader);
            for (int j = 1; j < 20; ++j) {
                Method method = testClass.getDeclaredMethod("test" + j);
                method.invoke(null);
                method.invoke(null);
            }
            System.gc();
        }
    }

    public static void runA() throws Throwable {
        Class<?> thisClass = TestOverloadCompileQueues.class;
        ClassLoader defaultLoader = thisClass.getClassLoader();
        URL classesDir = thisClass.getProtectionDomain().getCodeSource().getLocation();
        for (int i = 0; i < ITERS_A; ++i) {
            URLClassLoader myLoader = URLClassLoader.newInstance(new URL[] { classesDir }, defaultLoader.getParent());
            Class<?> testClass = Class.forName(thisClass.getCanonicalName(), true, myLoader);
            for (int j = 0; j < 50; ++j) {
                Method method = testClass.getDeclaredMethod("testA" + j);
                method.invoke(null);
                method.invoke(null);
            }
        }
    }
}
