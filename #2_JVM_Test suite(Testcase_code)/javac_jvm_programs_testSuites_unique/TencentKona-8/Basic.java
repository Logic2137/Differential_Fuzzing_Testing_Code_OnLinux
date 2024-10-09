

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Class;
import java.lang.String;
import java.lang.System;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import sun.misc.Unsafe;
import sun.misc.Contended;


public class Basic {

    private static final Unsafe U;
    private static int ADDRESS_SIZE;
    private static int HEADER_SIZE;

    static {
        
        try {
            Field unsafe = Unsafe.class.getDeclaredField("theUnsafe");
            unsafe.setAccessible(true);
            U = (Unsafe) unsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }

        
        
        
        try {
            long off1 = U.objectFieldOffset(CompressedOopsClass.class.getField("obj1"));
            long off2 = U.objectFieldOffset(CompressedOopsClass.class.getField("obj2"));
            ADDRESS_SIZE = (int) Math.abs(off2 - off1);
            HEADER_SIZE = (int) Math.min(off1, off2);
        } catch (NoSuchFieldException e) {
            ADDRESS_SIZE = -1;
        }
    }

    static class CompressedOopsClass {
        public Object obj1;
        public Object obj2;
    }

    public static boolean arePaddedPairwise(Class klass, String field1, String field2) throws Exception {
        Field f1 = klass.getDeclaredField(field1);
        Field f2 = klass.getDeclaredField(field2);

        if (isStatic(f1) != isStatic(f2)) {
            return true; 
        }

        int diff = offset(f1) - offset(f2);
        if (diff < 0) {
            
            return (offset(f2) - (offset(f1) + getSize(f1))) > 64;
        } else {
            
            return (offset(f1) - (offset(f2) + getSize(f2))) > 64;
        }
    }

    public static boolean isPadded(Class klass, String field1) throws Exception {
        Field f1 = klass.getDeclaredField(field1);

        if (isStatic(f1)) {
            return offset(f1) > 128 + 64;
        }

        return offset(f1) > 64;
    }

    public static boolean sameLayout(Class klass1, Class klass2) throws Exception {
        for (Field f1 : klass1.getDeclaredFields()) {
            Field f2 = klass2.getDeclaredField(f1.getName());
            if (offset(f1) != offset(f2)) {
                return false;
            }
        }

        for (Field f2 : klass1.getDeclaredFields()) {
            Field f1 = klass2.getDeclaredField(f2.getName());
            if (offset(f1) != offset(f2)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    public static int offset(Field field) {
        if (isStatic(field)) {
            return (int) U.staticFieldOffset(field);
        } else {
            return (int) U.objectFieldOffset(field);
        }
    }

    public static int getSize(Field field) {
        Class type = field.getType();
        if (type == byte.class)    { return 1; }
        if (type == boolean.class) { return 1; }
        if (type == short.class)   { return 2; }
        if (type == char.class)    { return 2; }
        if (type == int.class)     { return 4; }
        if (type == float.class)   { return 4; }
        if (type == long.class)    { return 8; }
        if (type == double.class)  { return 8; }
        return ADDRESS_SIZE;
    }

    public static void main(String[] args) throws Exception {
        boolean endResult = true;

        

        if (arePaddedPairwise(Test1.class, "int1", "int2") ||
                isPadded(Test1.class, "int1") ||
                isPadded(Test1.class, "int2")) {
            System.err.println("Test1 failed");
            endResult &= false;
        }

        if (!arePaddedPairwise(Test2.class, "int1", "int2") ||
                !isPadded(Test2.class, "int1") ||
                isPadded(Test2.class, "int2")) {
            System.err.println("Test2 failed");
            endResult &= false;
        }

        if (!arePaddedPairwise(Test3.class, "int1", "int2") ||
                !isPadded(Test3.class, "int1") ||
                !isPadded(Test3.class, "int2")) {
            System.err.println("Test3 failed");
            endResult &= false;
        }

        if (arePaddedPairwise(Test4.class, "int1", "int2") ||
                !isPadded(Test4.class, "int1") ||
                !isPadded(Test4.class, "int2")) {
            System.err.println("Test4 failed");
            endResult &= false;
        }

        if (!arePaddedPairwise(Test5.class, "int1", "int2") ||
                !isPadded(Test5.class, "int1") ||
                !isPadded(Test5.class, "int2")) {
            System.err.println("Test5 failed");
            endResult &= false;
        }

        if (!arePaddedPairwise(Test6.class, "int1", "int2") ||
                !isPadded(Test6.class, "int1") ||
                !isPadded(Test6.class, "int2")) {
            System.err.println("Test6 failed");
            endResult &= false;
        }

        if (arePaddedPairwise(Test7.class, "int1", "int2") ||
                !isPadded(Test7.class, "int1") ||
                !isPadded(Test7.class, "int2")) {
            System.err.println("Test7 failed");
            endResult &= false;
        }

        if (!arePaddedPairwise(Test8.class, "int1", "int2") ||
                !isPadded(Test8.class, "int1") ||
                !isPadded(Test8.class, "int2")) {
            System.err.println("Test8 failed");
            endResult &= false;
        }

        if (!arePaddedPairwise(Test9.class, "int1", "int2") ||
                !isPadded(Test9.class, "int1") ||
                !isPadded(Test9.class, "int2")) {
            System.err.println("Test9 failed");
            endResult &= false;
        }

        if (!sameLayout(Test4.class, Test7.class)) {
            System.err.println("Test4 and Test7 have different layouts");
            endResult &= false;
        }

        if (!sameLayout(Test5.class, Test6.class)) {
            System.err.println("Test5 and Test6 have different layouts");
            endResult &= false;
        }

        if (!sameLayout(Test8.class, Test9.class)) {
            System.err.println("Test8 and Test9 have different layouts");
            endResult &= false;
        }

        System.out.println(endResult ? "Test PASSES" : "Test FAILS");
        if (!endResult) {
           throw new Error("Test failed");
        }
    }

    

    
    public static class Test1 {
                                 private int int1;
                                 private int int2;
    }

    
    public static class Test2 {
        @Contended               private int int1;
                                 private int int2;
    }

    
    public static class Test3 {
        @Contended               private int int1;
        @Contended               private int int2;
    }

    
    public static class Test4 {
        @Contended("sameGroup")  private int int1;
        @Contended("sameGroup")  private int int2;
    }

    
    public static class Test5 {
        @Contended("diffGroup1") private int int1;
        @Contended("diffGroup2") private int int2;
    }

    
    public static class Test6 {
        @Contended               private int int1;
        @Contended("diffGroup2") private int int2;
    }

    
    @Contended
    public static class Test7 {
                                 private int int1;
                                 private int int2;
    }

    
    @Contended
    public static class Test8 {
        @Contended               private int int1;
                                 private int int2;
    }

    
    @Contended
    public static class Test9 {
        @Contended("group")      private int int1;
                                 private int int2;
    }

}

