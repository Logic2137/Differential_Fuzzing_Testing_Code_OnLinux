

import java.io.Serializable;
import java.lang.invoke.*;
import java.util.concurrent.Callable;


public class StringConcatFactoryInvariants {

    private static final char TAG_ARG   = '\u0001';
    private static final char TAG_CONST = '\u0002';

    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        String methodName = "foo";
        MethodType mt = MethodType.methodType(String.class, String.class, int.class);
        String recipe = "" + TAG_ARG + TAG_ARG + TAG_CONST;
        Object[][] constants = new Object[][] {
                new String[] { "bar" },
                new Integer[] { 1 },
                new Short[] { 2 },
                new Long[] { 3L },
                new Boolean[] { true },
                new Character[] { 'a' },
                new Byte[] { -128 },
                new Class[] { String.class },
                new MethodHandle[] { MethodHandles.constant(String.class, "constant") },
                new MethodType[] { MethodType.methodType(String.class) }
        };
        
        
        String[] constantString = new String[] {
                "bar",
                "1",
                "2",
                "3",
                "true",
                "a",
                "-128",
                "class java.lang.String",
                "MethodHandle()String",
                "()String"
        };


        final int LIMIT = 200;

        
        Class<?>[] underThreshold = new Class<?>[LIMIT - 1];
        Class<?>[] threshold      = new Class<?>[LIMIT];
        Class<?>[] overThreshold  = new Class<?>[LIMIT + 1];

        StringBuilder sbUnderThreshold = new StringBuilder();
        sbUnderThreshold.append(TAG_CONST);
        for (int c = 0; c < LIMIT - 1; c++) {
            underThreshold[c] = int.class;
            threshold[c] = int.class;
            overThreshold[c] = int.class;
            sbUnderThreshold.append(TAG_ARG);
        }
        threshold[LIMIT - 1] = int.class;
        overThreshold[LIMIT - 1] = int.class;
        overThreshold[LIMIT] = int.class;

        String recipeEmpty = "";
        String recipeUnderThreshold = sbUnderThreshold.toString();
        String recipeThreshold = sbUnderThreshold.append(TAG_ARG).toString();
        String recipeOverThreshold = sbUnderThreshold.append(TAG_ARG).toString();

        MethodType mtEmpty = MethodType.methodType(String.class);
        MethodType mtUnderThreshold = MethodType.methodType(String.class, underThreshold);
        MethodType mtThreshold = MethodType.methodType(String.class, threshold);
        MethodType mtOverThreshold = MethodType.methodType(String.class, overThreshold);


        
        {
            CallSite cs = StringConcatFactory.makeConcat(lookup, methodName, mt);
            test("foo42", (String) cs.getTarget().invokeExact("foo", 42));
        }

        {
            for (int i = 0; i < constants.length; i++) {
                CallSite cs = StringConcatFactory.makeConcatWithConstants(lookup, methodName, mt, recipe, constants[i]);
                test("foo42".concat(constantString[i]), (String) cs.getTarget().invokeExact("foo", 42));
            }
        }

        
        failNPE("Lookup is null",
                () -> StringConcatFactory.makeConcat(null, methodName, mt));

        failNPE("Method name is null",
                () -> StringConcatFactory.makeConcat(lookup, null, mt));

        failNPE("MethodType is null",
                () -> StringConcatFactory.makeConcat(lookup, methodName, null));

        
        for (int i = 0; i < constants.length; i++) {
            final Object[] consts = constants[i];

            failNPE("Lookup is null",
                    () -> StringConcatFactory.makeConcatWithConstants(null, methodName, mt, recipe, consts));

            failNPE("Method name is null",
                    () -> StringConcatFactory.makeConcatWithConstants(lookup, null, mt, recipe, consts));

            failNPE("MethodType is null",
                    () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, null, recipe, consts));

            failNPE("Recipe is null",
                    () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mt, null, consts));
        }

        failNPE("Constants vararg is null",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mt, recipe, (Object[]) null));

        failNPE("Constant argument is null",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mt, recipe, new Object[] { null }));

        
        fail("Return type: void",
                () -> StringConcatFactory.makeConcat(lookup, methodName, MethodType.methodType(void.class, String.class, int.class)));

        fail("Return type: int",
                () -> StringConcatFactory.makeConcat(lookup, methodName, MethodType.methodType(int.class, String.class, int.class)));

        fail("Return type: StringBuilder",
                () -> StringConcatFactory.makeConcat(lookup, methodName, MethodType.methodType(StringBuilder.class, String.class, int.class)));

        ok("Return type: Object",
                () -> StringConcatFactory.makeConcat(lookup, methodName, MethodType.methodType(Object.class, String.class, int.class)));

        ok("Return type: CharSequence",
                () -> StringConcatFactory.makeConcat(lookup, methodName, MethodType.methodType(CharSequence.class, String.class, int.class)));

        ok("Return type: Serializable",
                () -> StringConcatFactory.makeConcat(lookup, methodName, MethodType.methodType(Serializable.class, String.class, int.class)));

        
        for (int i = 0; i < constants.length; i++) {
            final Object[] consts = constants[i];
            fail("Return type: void",
                    () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, MethodType.methodType(void.class, String.class, int.class), recipe, consts));

            fail("Return type: int",
                    () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, MethodType.methodType(int.class, String.class, int.class), recipe, consts));

            fail("Return type: StringBuilder",
                    () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, MethodType.methodType(StringBuilder.class, String.class, int.class), recipe, consts));

            ok("Return type: Object",
                    () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, MethodType.methodType(Object.class, String.class, int.class), recipe, consts));

            ok("Return type: CharSequence",
                    () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, MethodType.methodType(CharSequence.class, String.class, int.class), recipe, consts));

            ok("Return type: Serializable",
                    () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, MethodType.methodType(Serializable.class, String.class, int.class), recipe, consts));
        }

        
        ok("Dynamic arguments is under limit",
                () -> StringConcatFactory.makeConcat(lookup, methodName, mtUnderThreshold));

        ok("Dynamic arguments is at the limit",
                () -> StringConcatFactory.makeConcat(lookup, methodName, mtThreshold));

        fail("Dynamic arguments is over the limit",
                () -> StringConcatFactory.makeConcat(lookup, methodName, mtOverThreshold));

        
        ok("Dynamic arguments is under limit",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mtUnderThreshold, recipeUnderThreshold, constants[0]));

        ok("Dynamic arguments is at the limit",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mtThreshold, recipeThreshold, constants[0]));

        fail("Dynamic arguments is over the limit",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mtOverThreshold, recipeOverThreshold, constants[0]));

        
        ok("Static arguments and recipe match",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mtThreshold, recipeThreshold, "bar"));

        fail("Static arguments and recipe mismatch",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mtThreshold, recipeThreshold, "bar", "baz"));

        
        fail("Dynamic arguments and recipe mismatch",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mtThreshold, recipeUnderThreshold, constants[0]));

        ok("Dynamic arguments and recipe match",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mtThreshold, recipeThreshold, constants[0]));

        fail("Dynamic arguments and recipe mismatch",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mtThreshold, recipeOverThreshold, constants[0]));

        
        {
            Object[] arg = {"boo", "bar"};

            CallSite cs1 = StringConcatFactory.makeConcatWithConstants(lookup, methodName, MethodType.methodType(String.class, int.class), "" + TAG_ARG + TAG_CONST + TAG_CONST, arg);
            test("42boobar", (String) cs1.getTarget().invokeExact(42));
        }

        
        ok("Can pass regular constants",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, MethodType.methodType(String.class, int.class), "" + TAG_ARG + TAG_CONST, "foo"));

        failNPE("Cannot pass null constants",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, MethodType.methodType(String.class, int.class), "" + TAG_ARG + TAG_CONST, new Object[]{null}));

        
        ok("Ok to pass empty arguments",
                () -> StringConcatFactory.makeConcat(lookup, methodName, mtEmpty));

        
        ok("Ok to pass empty arguments",
                () -> StringConcatFactory.makeConcatWithConstants(lookup, methodName, mtEmpty, recipeEmpty));

        
        fail("Passing public Lookup",
                () -> StringConcatFactory.makeConcat(MethodHandles.publicLookup(), methodName, mtEmpty));

        
        fail("Passing public Lookup",
                () -> StringConcatFactory.makeConcatWithConstants(MethodHandles.publicLookup(), methodName, mtEmpty, recipeEmpty));

        
        {
            MethodType zero = MethodType.methodType(String.class);
            CallSite cs = StringConcatFactory.makeConcat(lookup, methodName, zero);
            test("", (String) cs.getTarget().invokeExact());

            cs = StringConcatFactory.makeConcatWithConstants(lookup, methodName, zero, "");
            test("", (String) cs.getTarget().invokeExact());
        }

        
        {
            MethodType zero = MethodType.methodType(String.class);
            MethodType one = MethodType.methodType(String.class, String.class);
            CallSite cs = StringConcatFactory.makeConcat(lookup, methodName, one);
            test("A", (String) cs.getTarget().invokeExact("A"));

            cs = StringConcatFactory.makeConcatWithConstants(lookup, methodName, one, "\1");
            test("A", (String) cs.getTarget().invokeExact("A"));

            cs = StringConcatFactory.makeConcatWithConstants(lookup, methodName, zero, "\2", "A");
            test("A", (String) cs.getTarget().invokeExact());
        }
    }

    public static void ok(String msg, Callable runnable) {
        try {
            runnable.call();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException(msg + ", should have passed", e);
        }
    }

    public static void fail(String msg, Callable runnable) {
        boolean expected = false;
        try {
            runnable.call();
        } catch (StringConcatException e) {
            expected = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (!expected) {
            throw new IllegalStateException(msg + ", should have failed with StringConcatException");
        }
    }


    public static void failNPE(String msg, Callable runnable) {
        boolean expected = false;
        try {
            runnable.call();
        } catch (NullPointerException e) {
            expected = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (!expected) {
            throw new IllegalStateException(msg + ", should have failed with NullPointerException");
        }
    }

    public static void test(String expected, String actual) {
       
       if (!expected.equals(actual)) {
           StringBuilder sb = new StringBuilder();
           sb.append("Expected = ");
           sb.append(expected);
           sb.append(", actual = ");
           sb.append(actual);
           throw new IllegalStateException(sb.toString());
       }
    }

}
