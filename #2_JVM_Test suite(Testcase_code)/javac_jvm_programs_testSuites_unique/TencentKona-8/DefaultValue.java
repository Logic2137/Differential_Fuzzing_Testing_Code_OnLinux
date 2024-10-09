

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


public class DefaultValue {

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
        Field f1 = klass.getField(field1);
        Field f2 = klass.getField(field2);

        int diff = offset(f1) - offset(f2);
        if (diff < 0) {
            
            return (offset(f2) - (offset(f1) + getSize(f1))) > 64;
        } else {
            
            return (offset(f1) - (offset(f2) + getSize(f2))) > 64;
        }
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

        if (!arePaddedPairwise(R1.class, "int1", "int2")) {
            System.err.println("R1 failed");
            endResult &= false;
        }

        if (!arePaddedPairwise(R2.class, "int1", "int2")) {
            System.err.println("R2 failed");
            endResult &= false;
        }

        if (!arePaddedPairwise(R3.class, "int1", "int2")) {
            System.err.println("R3 failed");
            endResult &= false;
        }

        System.out.println(endResult ? "Test PASSES" : "Test FAILS");
        if (!endResult) {
           throw new Error("Test failed");
        }
    }

    public static class R1 {
        @Contended
        public int int1;
        @Contended
        public int int2;
    }

    public static class R2 {
        @Contended("")
        public int int1;
        @Contended("")
        public int int2;
    }

    public static class R3 {
        @Contended()
        public int int1;
        @Contended()
        public int int2;
    }

}

