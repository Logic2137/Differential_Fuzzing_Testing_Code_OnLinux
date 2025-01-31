import java.lang.invoke.*;

public class Test6987555 {

    private static final Class CLASS = Test6987555.class;

    private static final String NAME = "foo";

    private static final boolean DEBUG = false;

    public static void main(String[] args) throws Throwable {
        testboolean();
        testbyte();
        testchar();
        testshort();
        testint();
    }

    static void testboolean() throws Throwable {
        doboolean(false);
        doboolean(true);
    }

    static void doboolean(boolean x) throws Throwable {
        if (DEBUG)
            System.out.println("boolean=" + x);
        MethodHandle mh1 = MethodHandles.lookup().findStatic(CLASS, NAME, MethodType.methodType(boolean.class, boolean.class));
        MethodHandle mh2 = mh1.asType(MethodType.methodType(boolean.class, Boolean.class));
        boolean a = (boolean) mh1.invokeExact(x);
        boolean b = (boolean) mh2.invokeExact(Boolean.valueOf(x));
        assert a == b : a + " != " + b;
    }

    static void testbyte() throws Throwable {
        byte[] a = new byte[] { Byte.MIN_VALUE, Byte.MIN_VALUE + 1, -0x0F, -1, 0, 1, 0x0F, Byte.MAX_VALUE - 1, Byte.MAX_VALUE };
        for (int i = 0; i < a.length; i++) {
            dobyte(a[i]);
        }
    }

    static void dobyte(byte x) throws Throwable {
        if (DEBUG)
            System.out.println("byte=" + x);
        MethodHandle mh1 = MethodHandles.lookup().findStatic(CLASS, NAME, MethodType.methodType(byte.class, byte.class));
        MethodHandle mh2 = mh1.asType(MethodType.methodType(byte.class, Byte.class));
        byte a = (byte) mh1.invokeExact(x);
        byte b = (byte) mh2.invokeExact(Byte.valueOf(x));
        assert a == b : a + " != " + b;
    }

    static void testchar() throws Throwable {
        char[] a = new char[] { Character.MIN_VALUE, Character.MIN_VALUE + 1, 0x000F, 0x00FF, 0x0FFF, Character.MAX_VALUE - 1, Character.MAX_VALUE };
        for (int i = 0; i < a.length; i++) {
            dochar(a[i]);
        }
    }

    static void dochar(char x) throws Throwable {
        if (DEBUG)
            System.out.println("char=" + x);
        MethodHandle mh1 = MethodHandles.lookup().findStatic(CLASS, NAME, MethodType.methodType(char.class, char.class));
        MethodHandle mh2 = mh1.asType(MethodType.methodType(char.class, Character.class));
        char a = (char) mh1.invokeExact(x);
        char b = (char) mh2.invokeExact(Character.valueOf(x));
        assert a == b : a + " != " + b;
    }

    static void testshort() throws Throwable {
        short[] a = new short[] { Short.MIN_VALUE, Short.MIN_VALUE + 1, -0x0FFF, -0x00FF, -0x000F, -1, 0, 1, 0x000F, 0x00FF, 0x0FFF, Short.MAX_VALUE - 1, Short.MAX_VALUE };
        for (int i = 0; i < a.length; i++) {
            doshort(a[i]);
        }
    }

    static void doshort(short x) throws Throwable {
        if (DEBUG)
            System.out.println("short=" + x);
        MethodHandle mh1 = MethodHandles.lookup().findStatic(CLASS, NAME, MethodType.methodType(short.class, short.class));
        MethodHandle mh2 = mh1.asType(MethodType.methodType(short.class, Short.class));
        short a = (short) mh1.invokeExact(x);
        short b = (short) mh2.invokeExact(Short.valueOf(x));
        assert a == b : a + " != " + b;
    }

    static void testint() throws Throwable {
        int[] a = new int[] { Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -0x00000FFF, -0x000000FF, -0x0000000F, -1, 0, 1, 0x0000000F, 0x000000FF, 0x00000FFF, Integer.MAX_VALUE - 1, Integer.MAX_VALUE };
        for (int i = 0; i < a.length; i++) {
            doint(a[i]);
        }
    }

    static void doint(int x) throws Throwable {
        if (DEBUG)
            System.out.println("int=" + x);
        MethodHandle mh1 = MethodHandles.lookup().findStatic(CLASS, NAME, MethodType.methodType(int.class, int.class));
        MethodHandle mh2 = mh1.asType(MethodType.methodType(int.class, Integer.class));
        int a = (int) mh1.invokeExact(x);
        int b = (int) mh2.invokeExact(Integer.valueOf(x));
        assert a == b : a + " != " + b;
    }

    public static boolean foo(boolean i) {
        return i;
    }

    public static byte foo(byte i) {
        return i;
    }

    public static char foo(char i) {
        return i;
    }

    public static short foo(short i) {
        return i;
    }

    public static int foo(int i) {
        return i;
    }
}
