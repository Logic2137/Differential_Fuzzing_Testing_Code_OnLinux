



import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;

import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;
import static java.lang.invoke.MethodHandleInfo.*;

public class Test7087570 {

    private static final TestMethodData[] TESTS = new TestMethodData[] {
        
        data(DummyFieldHolder.class, "instanceField", getterMethodType(String.class), DummyFieldHolder.class, REF_getField),
        data(DummyFieldHolder.class, "instanceField", setterMethodType(String.class), DummyFieldHolder.class, REF_putField),
        data(DummyFieldHolder.class, "staticField", getterMethodType(Integer.class), DummyFieldHolder.class, REF_getStatic),
        data(DummyFieldHolder.class, "staticField", setterMethodType(Integer.class), DummyFieldHolder.class, REF_putStatic),
        data(DummyFieldHolder.class, "instanceByteField", getterMethodType(byte.class), DummyFieldHolder.class, REF_getField),
        data(DummyFieldHolder.class, "instanceByteField", setterMethodType(byte.class), DummyFieldHolder.class, REF_putField),

        
        data(Object.class, "hashCode", methodType(int.class), Object.class, REF_invokeVirtual),

        
        
        data(String.class, "hashCode", methodType(int.class), String.class, REF_invokeVirtual),

        
        data(Collections.class, "sort", methodType(void.class, List.class), Collections.class, REF_invokeStatic),
        data(Arrays.class, "asList", methodType(List.class, Object[].class), Arrays.class, REF_invokeStatic), 

        
        data(Object.class, "hashCode", methodType(int.class), Object.class, REF_invokeSpecial),

        
        data(String.class, "<init>", methodType(void.class, char[].class), String.class, REF_newInvokeSpecial),
        data(DummyFieldHolder.class, "<init>", methodType(void.class, byte.class, Long[].class), DummyFieldHolder.class, REF_newInvokeSpecial), 

        
        data(List.class, "size", methodType(int.class), List.class, REF_invokeInterface)
    };

    public static void main(String... args) throws Throwable {
        testWithLookup();
        testWithUnreflect();
    }

    private static void doTest(MethodHandle mh, TestMethodData testMethod) {
        MethodHandleInfo mhi = LOOKUP.revealDirect(mh);

        System.out.printf("%s.%s: %s, nominal refKind: %s, actual refKind: %s\n",
                          testMethod.clazz.getName(), testMethod.name, testMethod.methodType,
                          referenceKindToString(testMethod.referenceKind),
                          referenceKindToString(mhi.getReferenceKind()));
        assertEquals(testMethod.name,           mhi.getName());
        assertEquals(testMethod.methodType,     mhi.getMethodType());
        assertEquals(testMethod.declaringClass, mhi.getDeclaringClass());
        assertEquals(testMethod.referenceKind == REF_invokeSpecial, isInvokeSpecial(mh));
        assertRefKindEquals(testMethod.referenceKind,  mhi.getReferenceKind());
    }

    private static void testWithLookup() throws Throwable {
        for (TestMethodData testMethod : TESTS) {
            MethodHandle mh = lookupFrom(testMethod);
            doTest(mh, testMethod);
        }
    }

    private static void testWithUnreflect() throws Throwable {
        for (TestMethodData testMethod : TESTS) {
            MethodHandle mh = unreflectFrom(testMethod);
            doTest(mh, testMethod);
        }
    }

    private static MethodType getterMethodType(Class<?> clazz) {
        return methodType(clazz);
    }

    private static MethodType setterMethodType(Class<?> clazz) {
        return methodType(void.class, clazz);
    }

    private static final Lookup LOOKUP = lookup();

    private static class TestMethodData {
        final Class<?> clazz;
        final String name;
        final MethodType methodType;
        final Class<?> declaringClass;
        final int referenceKind; 

        public TestMethodData(Class<?> clazz, String name,
                        MethodType methodType, Class<?> declaringClass,
                        int referenceKind) {
            this.clazz = clazz;
            this.name = name;
            this.methodType = methodType;
            this.declaringClass = declaringClass;
            this.referenceKind = referenceKind;
        }
    }

    private static TestMethodData data(Class<?> clazz, String name,
                                       MethodType methodType, Class<?> declaringClass,
                                       int referenceKind) {
        return new TestMethodData(clazz, name, methodType, declaringClass, referenceKind);
    }

    private static MethodHandle lookupFrom(TestMethodData testMethod)
            throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        switch (testMethod.referenceKind) {
        case REF_getField:
            return LOOKUP.findGetter(testMethod.clazz, testMethod.name, testMethod.methodType.returnType());
        case REF_putField:
            return LOOKUP.findSetter(testMethod.clazz, testMethod.name, testMethod.methodType.parameterType(0));
        case REF_getStatic:
            return LOOKUP.findStaticGetter(testMethod.clazz, testMethod.name, testMethod.methodType.returnType());
        case REF_putStatic:
            return LOOKUP.findStaticSetter(testMethod.clazz, testMethod.name, testMethod.methodType.parameterType(0));
        case REF_invokeVirtual:
        case REF_invokeInterface:
            return LOOKUP.findVirtual(testMethod.clazz, testMethod.name, testMethod.methodType);
        case REF_invokeStatic:
            return LOOKUP.findStatic(testMethod.clazz, testMethod.name, testMethod.methodType);
        case REF_invokeSpecial:
            Class<?> thisClass = LOOKUP.lookupClass();
            MethodHandle smh = LOOKUP.findSpecial(testMethod.clazz, testMethod.name, testMethod.methodType, thisClass);
            noteInvokeSpecial(smh);
            return smh;
        case REF_newInvokeSpecial:
            return LOOKUP.findConstructor(testMethod.clazz, testMethod.methodType);
        default:
            throw new Error("ERROR: unexpected referenceKind in test data");
        }
    }

    private static MethodHandle unreflectFrom(TestMethodData testMethod)
            throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        switch (testMethod.referenceKind) {
        case REF_getField:
        case REF_getStatic: {
                Field f = testMethod.clazz.getDeclaredField(testMethod.name);
                return LOOKUP.unreflectGetter(f);
            }
        case REF_putField:
        case REF_putStatic: {
                Field f = testMethod.clazz.getDeclaredField(testMethod.name);
                return LOOKUP.unreflectSetter(f);
            }
        case REF_invokeVirtual:
        case REF_invokeStatic:
        case REF_invokeInterface: {
                Method m = testMethod.clazz.getDeclaredMethod(testMethod.name, testMethod.methodType.parameterArray());
                return LOOKUP.unreflect(m);
            }
        case REF_invokeSpecial: {
                Method m = testMethod.clazz.getDeclaredMethod(testMethod.name, testMethod.methodType.parameterArray());
                Class<?> thisClass = LOOKUP.lookupClass();
                MethodHandle smh = LOOKUP.unreflectSpecial(m, thisClass);
                noteInvokeSpecial(smh);
                return smh;
            }
        case REF_newInvokeSpecial: {
                Constructor c = testMethod.clazz.getDeclaredConstructor(testMethod.methodType.parameterArray());
                return LOOKUP.unreflectConstructor(c);
            }
        default:
            throw new Error("ERROR: unexpected referenceKind in test data");
        }
    }

    private static List<MethodHandle> specialMethodHandles = new ArrayList<>();
    private static void noteInvokeSpecial(MethodHandle mh) {
        specialMethodHandles.add(mh);
        assert(isInvokeSpecial(mh));
    }
    private static boolean isInvokeSpecial(MethodHandle mh) {
        return specialMethodHandles.contains(mh);
    }

    private static void assertRefKindEquals(int expect, int observed) {
        if (expect == observed) return;

        String msg = "expected " + referenceKindToString(expect) +
                     " but observed " + referenceKindToString(observed);
        System.out.println("FAILED: " + msg);
        throw new AssertionError(msg);
    }

    private static void assertEquals(Object expect, Object observed) {
        if (java.util.Objects.equals(expect, observed)) return;

        String msg = "expected " + expect + " but observed " + observed;
        System.out.println("FAILED: " + msg);
        throw new AssertionError(msg);
    }
}

class DummyFieldHolder {
    public static Integer staticField;
    public String instanceField;
    public byte instanceByteField;

    public DummyFieldHolder(byte unused1, Long... unused2) {
    }
}

