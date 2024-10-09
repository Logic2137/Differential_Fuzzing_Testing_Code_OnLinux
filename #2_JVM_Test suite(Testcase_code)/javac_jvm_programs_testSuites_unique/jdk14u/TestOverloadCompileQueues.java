



package compiler.classUnloading.methodUnloading;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class TestOverloadCompileQueues {
    public static final int ITERS = 500; 

    
    public static void test0() { }
    public static void test1() { }
    public static void test2() { }
    public static void test3() { }
    public static void test4() { }
    public static void test5() { }
    public static void test6() { }
    public static void test7() { }
    public static void test8() { }
    public static void test9() { }
    public static void test10() { }
    public static void test11() { }
    public static void test12() { }
    public static void test13() { }
    public static void test14() { }
    public static void test15() { }
    public static void test16() { }
    public static void test17() { }
    public static void test18() { }
    public static void test19() { }

    public static void main(String[] args) throws Throwable {
        Class<?> thisClass = TestOverloadCompileQueues.class;
        ClassLoader defaultLoader = thisClass.getClassLoader();
        URL classesDir = thisClass.getProtectionDomain().getCodeSource().getLocation();

        for (int i = 0; i < ITERS; ++i) {
            
            URLClassLoader myLoader = URLClassLoader.newInstance(new URL[] {classesDir}, defaultLoader.getParent());
            Class<?> testClass = Class.forName(thisClass.getCanonicalName(), true, myLoader);

            
            for (int j = 1; j < 20; ++j) {
                Method method = testClass.getDeclaredMethod("test" + j);
                method.invoke(null);
                method.invoke(null);
            }

            
            System.gc();
        }
    }
}
