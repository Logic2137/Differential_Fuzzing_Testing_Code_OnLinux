



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class TestMethodReflectValueOf {

    public static void main(String[] args) {
        
        
        
        
        
        
        boolean checkIdentity = Boolean.getBoolean("sun.reflect.noInflation");

        
        testMethod(Boolean.TYPE, Boolean.FALSE, checkIdentity);
        testMethod(Boolean.TYPE, Boolean.TRUE, checkIdentity);

        
        for (int b = Byte.MIN_VALUE; b < (Byte.MAX_VALUE + 1); b++) {
            testMethod(Byte.TYPE, Byte.valueOf((byte) b), checkIdentity);
        }

        
        for (char c = '\u0000'; c <= '\u007F'; c++) {
            testMethod(Character.TYPE, Character.valueOf(c), checkIdentity);
        }

        
        for (int i = -128; i <= 127; i++) {
            testMethod(Integer.TYPE, Integer.valueOf(i), checkIdentity);
        }

        
        for (long l = -128L; l <= 127L; l++) {
            testMethod(Long.TYPE, Long.valueOf(l), checkIdentity);
        }

        
        for (short s = -128; s <= 127; s++) {
            testMethod(Short.TYPE, Short.valueOf(s), checkIdentity);
        }
    }

    public static void testMethod(Class<?> primType, Object wrappedValue,
            boolean checkIdentity)
    {
        String methodName = primType.getName() + "Method";
        try {
            Method method = TestMethodReflectValueOf.class.getMethod(methodName, primType);
            Object result = method.invoke(new TestMethodReflectValueOf(), wrappedValue);
            if (checkIdentity) {
                if (result != wrappedValue) {
                    throw new RuntimeException("The value " + wrappedValue
                        + " is not cached for the type " + primType);
                }
            } else {
                if (!result.equals(wrappedValue)) {
                    throw new RuntimeException("The result value " + result
                        + " is not equal to the expected value "
                        + wrappedValue + " for the type " + primType);
                }
            }
        } catch (  NoSuchMethodException | SecurityException
                 | IllegalAccessException | IllegalArgumentException
                 | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    public int intMethod(int value) {
        return value;
    }

    public long longMethod(long value) {
        return value;
    }

    public short shortMethod(short value) {
        return value;
    }

    public byte byteMethod(byte value) {
        return value;
    }

    public char charMethod(char value) {
        return value;
    }

    public boolean booleanMethod(boolean value) {
        return value;
    }

}
