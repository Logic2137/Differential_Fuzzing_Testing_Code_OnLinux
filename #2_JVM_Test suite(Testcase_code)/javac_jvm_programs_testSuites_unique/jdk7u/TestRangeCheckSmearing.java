



import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class TestRangeCheckSmearing {
    @Retention(RetentionPolicy.RUNTIME)
    @interface Args { int[] value(); }

    
    @Args({0, 8})
    static int m1(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+9];
        if (allaccesses) {
            res += array[i+8];
            res += array[i+7];
            res += array[i+6];
            res += array[i+5];
            res += array[i+4];
            res += array[i+3];
            res += array[i+2];
            res += array[i+1];
        }
        return res;
    }

    
    @Args({0, -9})
    static int m2(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+1];
        if (allaccesses) {
            res += array[i+2];
            res += array[i+3];
            res += array[i+4];
            res += array[i+5];
            res += array[i+6];
            res += array[i+7];
            res += array[i+8];
            res += array[i+9];
        }
        return res;
    }

    
    @Args({0, 8})
    static int m3(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+3];
        if (allaccesses) {
            res += array[i+2];
            res += array[i+1];
            res += array[i+4];
            res += array[i+5];
            res += array[i+6];
            res += array[i+7];
            res += array[i+8];
            res += array[i+9];
        }
        return res;
    }

    @Args({0, -9})
    static int m4(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+3];
        if (allaccesses) {
            res += array[i+4];
            res += array[i+1];
            res += array[i+2];
            res += array[i+5];
            res += array[i+6];
            res += array[i+7];
            res += array[i+8];
            res += array[i+9];
        }
        return res;
    }

    @Args({0, -3})
    static int m5(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+3];
        res += array[i+2];
        if (allaccesses) {
            res += array[i+1];
            res += array[i+4];
            res += array[i+5];
            res += array[i+6];
            res += array[i+7];
            res += array[i+8];
            res += array[i+9];
        }
        return res;
    }

    @Args({0, 6})
    static int m6(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+3];
        res += array[i+4];
        if (allaccesses) {
            res += array[i+2];
            res += array[i+1];
            res += array[i+5];
            res += array[i+6];
            res += array[i+7];
            res += array[i+8];
            res += array[i+9];
        }
        return res;
    }

    @Args({0, 6})
    static int m7(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+3];
        res += array[i+2];
        res += array[i+4];
        if (allaccesses) {
            res += array[i+1];
            res += array[i+5];
            res += array[i+6];
            res += array[i+7];
            res += array[i+8];
            res += array[i+9];
        }
        return res;
    }

    @Args({0, -3})
    static int m8(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+3];
        res += array[i+4];
        res += array[i+2];
        if (allaccesses) {
            res += array[i+1];
            res += array[i+5];
            res += array[i+6];
            res += array[i+7];
            res += array[i+8];
            res += array[i+9];
        }
        return res;
    }

    @Args({6, 15})
    static int m9(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+3];
        if (allaccesses) {
            res += array[i-2];
            res += array[i-1];
            res += array[i-4];
            res += array[i-5];
            res += array[i-6];
        }
        return res;
    }

    @Args({3, 12})
    static int m10(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+3];
        if (allaccesses) {
            res += array[i-2];
            res += array[i-1];
            res += array[i-3];
            res += array[i+4];
            res += array[i+5];
            res += array[i+6];
        }
        return res;
    }

    @Args({3, -3})
    static int m11(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+3];
        res += array[i-2];
        if (allaccesses) {
            res += array[i+5];
            res += array[i+6];
        }
        return res;
    }

    @Args({3, 6})
    static int m12(int[] array, int i, boolean allaccesses) {
        int res = 0;
        res += array[i+3];
        res += array[i+6];
        if (allaccesses) {
            res += array[i-2];
            res += array[i-3];
        }
        return res;
    }

    
    
    @Args({0})
    static int m13(int[] array, int i, boolean ignore) {
        int res = 0;
        res += array[i+3];
        res += array[i+3];
        return res;
    }

    @Args({2, 0})
    static int m14(int[] array, int i, boolean ignore) {
        int res = 0;

        res += array[i];
        res += array[i-2];
        res += array[i]; 
        res += array[i-1]; 

        return res;
    }

    static int[] m15_dummy = new int[10];
    @Args({2, 0})
    static int m15(int[] array, int i, boolean ignore) {
        int res = 0;
        res += array[i];

        
        
        
        

        int[] array2 = m15_dummy;
        int j = 0;
        for (; j < 10; j++);
        if (j == 10) {
            array2 = array;
        }

        res += array2[i-2];
        res += array2[i];
        res += array2[i-1]; 

        return res;
    }

    @Args({2, 0})
    static int m16(int[] array, int i, boolean ignore) {
        int res = 0;

        res += array[i];
        res += array[i-1];
        res += array[i-1];
        res += array[i-2];

        return res;
    }

    @Args({2, 0})
    static int m17(int[] array, int i, boolean ignore) {
        int res = 0;

        res += array[i];
        res += array[i-2];
        res += array[i-2];
        res += array[i+2];
        res += array[i+2];
        res += array[i-1];
        res += array[i-1];

        return res;
    }

    static public void main(String[] args) {
        new TestRangeCheckSmearing().doTests();
    }
    boolean success = true;
    boolean exception = false;
    final int[] array = new int[10];
    final HashMap<String,Method> tests = new HashMap<>();
    {
        final Class<?> TEST_PARAM_TYPES[] = { int[].class, int.class, boolean.class };
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.getName().matches("m[0-9]+")) {
                assert(Modifier.isStatic(m.getModifiers())) : m;
                assert(m.getReturnType() == int.class) : m;
                assert(Arrays.equals(m.getParameterTypes(), TEST_PARAM_TYPES)) : m;
                tests.put(m.getName(), m);
            }
        }
    }

    void invokeTest(Method m, int[] array, int index, boolean z) {
        try {
            m.invoke(null, array, index, z);
        } catch (ReflectiveOperationException roe) {
            Throwable ex = roe.getCause();
            if (ex instanceof ArrayIndexOutOfBoundsException)
                throw (ArrayIndexOutOfBoundsException) ex;
            throw new AssertionError(roe);
        }
    }

    void doTest(String name) {
        Method m = tests.get(name);
        tests.remove(name);
        int[] args = m.getAnnotation(Args.class).value();
        int index0 = args[0], index1;
        boolean exceptionRequired = true;
        if (args.length == 2) {
            index1 = args[1];
        } else {
            
            assert(args.length == 1);
            assert(name.equals("m13"));
            exceptionRequired = false;
            index1 = index0;
        }
        
        
        for (int i = 0; i < 20000; i++) {
            invokeTest(m, array, index0, true);
        }

        exception = false;
        boolean test_success = true;
        try {
            invokeTest(m, array, index1, false);
        } catch(ArrayIndexOutOfBoundsException aioob) {
            exception = true;
            System.out.println("ArrayIndexOutOfBoundsException thrown in "+name);
        }
        if (!exception) {
            System.out.println("ArrayIndexOutOfBoundsException was not thrown in "+name);
        }

        if (exception != exceptionRequired) {
            System.out.println((exceptionRequired?"exception required but not thrown":"not exception required but thrown") + " in "+name);
            test_success = false;
        }

        if (!test_success) {
            success = false;
            System.out.println("TEST FAILED: "+name);
        }

    }
    void doTests() {
        doTest("m1");
        doTest("m2");
        doTest("m3");
        doTest("m4");
        doTest("m5");
        doTest("m6");
        doTest("m7");
        doTest("m8");
        doTest("m9");
        doTest("m10");
        doTest("m11");
        doTest("m12");
        doTest("m13");
        doTest("m14");
        doTest("m15");
        doTest("m16");
        doTest("m17");
        if (!success) {
            throw new RuntimeException("Some tests failed");
        }
        assert(tests.isEmpty()) : tests;
    }
}
