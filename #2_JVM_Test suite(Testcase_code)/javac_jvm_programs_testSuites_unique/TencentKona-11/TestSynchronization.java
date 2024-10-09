


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestSynchronization {

    
    private static final boolean BOOLEAN_VAL = true;
    private static final char CHAR_VAL = 'x';
    private static final char[] CHAR_ARRAY_VAL = {'c', 'h', 'a', 'r', 'a', 'r',
        'r', 'a', 'y'};
    private static final int INT_VAL = 1;
    private static final double DOUBLE_VAL = 1.0d;
    private static final float FLOAT_VAL = 1.0f;
    private static final long LONG_VAL = 1L;
    private static final Object OBJECT_VAL = new Object();
    private static final String STRING_VAL = "String value";
    private static final StringBuilder STRING_BUILDER_VAL =
            new StringBuilder("StringBuilder value");
    private static final StringBuffer STRING_BUFFER_VAL =
            new StringBuffer("StringBuffer value");
    private static final CharSequence[] CHAR_SEQUENCE_VAL = {STRING_VAL,
        STRING_BUILDER_VAL, STRING_BUFFER_VAL};

    public static void main(String... args) throws Exception {
        
        testClass(MyTestClass.class,  true);
        
        testClass(StringBuffer.class,  false);
    }

    
    private static void testClass(Class<?> aClass, boolean isSelfTest) throws
            Exception {
        
        
        
        List<Method> methods = Arrays.asList(aClass.getDeclaredMethods());
        for (Method m : methods) {
            
            if (m.isSynthetic()) {
                continue;
            }
            int modifiers = m.getModifiers();
            if (Modifier.isPublic(modifiers)
                    && !Modifier.isSynchronized(modifiers)) {
                try {
                    testMethod(aClass, m);
                } catch (TestFailedException e) {
                    if (isSelfTest) {
                        String methodName = e.getMethod().getName();
                        switch (methodName) {
                            case "should_pass":
                                throw new RuntimeException(
                                        "Test failed: self-test failed.  The 'should_pass' method did not pass the synchronization test. Check the test code.");
                            case "should_fail":
                                break;
                            default:
                                throw new RuntimeException(
                                        "Test failed: something is amiss with the test. A TestFailedException was generated on a call to "
                                        + methodName + " which we didn't expect to test in the first place.");
                        }
                    } else {
                        throw new RuntimeException("Test failed: the method "
                                + e.getMethod().toString()
                                + " should be synchronized, but isn't.");
                    }
                }
            }
        }
    }

    private static void invokeMethod(Class<?> aClass, final Method m,
            final Object[] args) throws TestFailedException, Exception {
        
        final Constructor<?> objConstructor;
        Object obj = null;

        objConstructor = aClass.getConstructor(String.class);
        obj = objConstructor.newInstance("LeftPalindrome-emordnilaP-thgiR");

        
        if (!isSynchronized(m, obj, args)) {
            throw new TestFailedException(m);
        }
    }

    private static void testMethod(Class<?> aClass, Method m) throws
            Exception {
        
        Class<?>[] pTypes = m.getParameterTypes();
        List<Integer> charSequenceArgs = new ArrayList<>();
        Object[] args = new Object[pTypes.length];
        for (int i = 0; i < pTypes.length; i++) {
            
            Class<?> pType = pTypes[i];
            if (pType.equals(boolean.class)) {
                args[i] = BOOLEAN_VAL;
            } else if (pType.equals(char.class)) {
                args[i] = CHAR_VAL;
            } else if (pType.equals(int.class)) {
                args[i] = INT_VAL;
            } else if (pType.equals(double.class)) {
                args[i] = DOUBLE_VAL;
            } else if (pType.equals(float.class)) {
                args[i] = FLOAT_VAL;
            } else if (pType.equals(long.class)) {
                args[i] = LONG_VAL;
            } else if (pType.equals(Object.class)) {
                args[i] = OBJECT_VAL;
            } else if (pType.equals(StringBuilder.class)) {
                args[i] = STRING_BUILDER_VAL;
            } else if (pType.equals(StringBuffer.class)) {
                args[i] = STRING_BUFFER_VAL;
            } else if (pType.equals(String.class)) {
                args[i] = STRING_VAL;
            } else if (pType.isArray() && pType.getComponentType().equals(char.class)) {
                args[i] = CHAR_ARRAY_VAL;
            } else if (pType.equals(CharSequence.class)) {
                charSequenceArgs.add(new Integer(i));
            } else {
                throw new RuntimeException("Test Failed: not accounting for method call with parameter type of " + pType.getName() + " You must update the test.");
            }
        }
        
        if (charSequenceArgs.isEmpty()) {
            invokeMethod(aClass, m, args);
        } else {
            
            if (charSequenceArgs.size() > 1) {
                throw new RuntimeException("Test Failed: the test cannot handle a method with multiple CharSequence arguments.  You must update the test to handle the method "
                        + m.toString());
            }
            for (int j = 0; j < CHAR_SEQUENCE_VAL.length; j++) {
                args[charSequenceArgs.get(0)] = CHAR_SEQUENCE_VAL[j];
                invokeMethod(aClass, m, args);
            }
        }
    }

    @SuppressWarnings("serial")
    private static class TestFailedException extends Exception {

        final Method m;

        public Method getMethod() {
            return m;
        }

        public TestFailedException(Method m) {
            this.m = m;
        }
    }

    static class InvokeTask implements Runnable {

        private final Method m;
        private final Object target;
        private final Object[] args;

        InvokeTask(Method m, Object target, Object... args) {
            this.m = m;
            this.target = target;
            this.args = args;
        }

        @Override
        public void run() {
            try {
                m.invoke(target, args);
            } catch (IllegalAccessException | IllegalArgumentException |
                    InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    
    private static boolean isSynchronized(Method m, Object target,
            Object... args) {
        Thread t = new Thread(new InvokeTask(m, target, args));

        Boolean isSynchronized = null;

        synchronized (target) {
            t.start();

            while (isSynchronized == null) {
                switch (t.getState()) {
                    case NEW:
                    case RUNNABLE:
                    case WAITING:
                    case TIMED_WAITING:
                        Thread.yield();
                        break;
                    case BLOCKED:
                        isSynchronized = true;
                        break;
                    case TERMINATED:
                        isSynchronized = false;
                        break;
                }
            }
        }

        try {
            t.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return isSynchronized;
    }

    
    private static class MyTestClass {

        @SuppressWarnings("unused")
        public MyTestClass(String s) {
        }

        @SuppressWarnings("unused")
        public void should_pass() {
            
            sync_shouldnt_be_tested();
        }

        @SuppressWarnings("unused")
        public void should_fail() {
        }

        public synchronized void sync_shouldnt_be_tested() {
        }
    }
}
