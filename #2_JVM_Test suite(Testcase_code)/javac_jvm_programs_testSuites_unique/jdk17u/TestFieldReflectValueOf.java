import java.lang.reflect.Field;

public class TestFieldReflectValueOf {

    @SuppressWarnings("unused")
    private static boolean booleanStaticField;

    @SuppressWarnings("unused")
    private static byte byteStaticField;

    @SuppressWarnings("unused")
    private static char charStaticField;

    @SuppressWarnings("unused")
    private static int intStaticField;

    @SuppressWarnings("unused")
    private static long longStaticField;

    @SuppressWarnings("unused")
    private static short shortStaticField;

    @SuppressWarnings("unused")
    private static volatile boolean booleanStaticVolatileField;

    @SuppressWarnings("unused")
    private static volatile byte byteStaticVolatileField;

    @SuppressWarnings("unused")
    private static volatile char charStaticVolatileField;

    @SuppressWarnings("unused")
    private static volatile int intStaticVolatileField;

    @SuppressWarnings("unused")
    private static volatile long longStaticVolatileField;

    @SuppressWarnings("unused")
    private static volatile short shortStaticVolatileField;

    @SuppressWarnings("unused")
    private boolean booleanField;

    @SuppressWarnings("unused")
    private byte byteField;

    @SuppressWarnings("unused")
    private char charField;

    @SuppressWarnings("unused")
    private int intField;

    @SuppressWarnings("unused")
    private long longField;

    @SuppressWarnings("unused")
    private short shortField;

    @SuppressWarnings("unused")
    private volatile boolean booleanVolatileField;

    @SuppressWarnings("unused")
    private volatile byte byteVolatileField;

    @SuppressWarnings("unused")
    private volatile char charVolatileField;

    @SuppressWarnings("unused")
    private volatile int intVolatileField;

    @SuppressWarnings("unused")
    private volatile long longVolatileField;

    @SuppressWarnings("unused")
    private volatile short shortVolatileField;

    public static void main(String[] args) {
        testUnsafeStaticFieldAccessors();
        testUnsafeQualifiedStaticFieldAccessors();
        testUnsafeFieldAccessors();
        testUnsafeQualifiedFieldAccessors();
    }

    private static void testUnsafeStaticFieldAccessors() {
        testFieldAccessors(true, false);
    }

    private static void testUnsafeQualifiedStaticFieldAccessors() {
        testFieldAccessors(true, true);
    }

    private static void testUnsafeFieldAccessors() {
        testFieldAccessors(false, false);
    }

    private static void testUnsafeQualifiedFieldAccessors() {
        testFieldAccessors(false, true);
    }

    private static void testFieldAccessors(boolean checkStatic, boolean checkVolatile) {
        testField(Boolean.TYPE, Boolean.FALSE, checkStatic, checkVolatile);
        testField(Boolean.TYPE, Boolean.TRUE, checkStatic, checkVolatile);
        for (int b = Byte.MIN_VALUE; b < (Byte.MAX_VALUE + 1); b++) {
            testField(Byte.TYPE, Byte.valueOf((byte) b), checkStatic, checkVolatile);
        }
        for (char c = '\u0000'; c <= '\u007F'; c++) {
            testField(Character.TYPE, Character.valueOf(c), checkStatic, checkVolatile);
        }
        for (int i = -128; i <= 127; i++) {
            testField(Integer.TYPE, Integer.valueOf(i), checkStatic, checkVolatile);
        }
        for (long l = -128L; l <= 127L; l++) {
            testField(Long.TYPE, Long.valueOf(l), checkStatic, checkVolatile);
        }
        for (short s = -128; s <= 127; s++) {
            testField(Short.TYPE, Short.valueOf(s), checkStatic, checkVolatile);
        }
    }

    private static void testField(Class<?> primType, Object wrappedValue, boolean checkStatic, boolean checkVolatile) {
        String fieldName = primType.getName();
        if (checkStatic) {
            fieldName += "Static";
        }
        if (checkVolatile) {
            fieldName += "Volatile";
        }
        fieldName += "Field";
        try {
            Field field = TestFieldReflectValueOf.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            TestFieldReflectValueOf obj = new TestFieldReflectValueOf();
            field.set(obj, wrappedValue);
            Object result = field.get(obj);
            if (result != wrappedValue) {
                throw new RuntimeException("The value " + wrappedValue + " is not cached for the type " + primType);
            }
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
