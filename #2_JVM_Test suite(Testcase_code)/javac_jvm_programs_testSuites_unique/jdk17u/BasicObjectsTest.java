import java.util.*;
import java.util.function.*;

public class BasicObjectsTest {

    public static void main(String... args) {
        int errors = 0;
        errors += testEquals();
        errors += testDeepEquals();
        errors += testHashCode();
        errors += testHash();
        errors += testToString();
        errors += testToString2();
        errors += testCompare();
        errors += testRequireNonNull();
        errors += testIsNull();
        errors += testNonNull();
        errors += testNonNullOf();
        if (errors > 0)
            throw new RuntimeException();
    }

    private static int testEquals() {
        int errors = 0;
        Object[] values = { null, "42", 42 };
        for (int i = 0; i < values.length; i++) for (int j = 0; j < values.length; j++) {
            boolean expected = (i == j);
            Object a = values[i];
            Object b = values[j];
            boolean result = Objects.equals(a, b);
            if (result != expected) {
                errors++;
                System.err.printf("When equating %s to %s, got %b instead of %b%n.", a, b, result, expected);
            }
        }
        return errors;
    }

    private static int testDeepEquals() {
        int errors = 0;
        Object[] values = { null, null, new byte[] { (byte) 1 }, new short[] { (short) 1 }, new int[] { 1 }, new long[] { 1L }, new char[] { (char) 1 }, new float[] { 1.0f }, new double[] { 1.0d }, new String[] { "one" } };
        values[1] = values;
        for (int i = 0; i < values.length; i++) for (int j = 0; j < values.length; j++) {
            boolean expected = (i == j);
            Object a = values[i];
            Object b = values[j];
            boolean result = Objects.deepEquals(a, b);
            if (result != expected) {
                errors++;
                System.err.printf("When equating %s to %s, got %b instead of %b%n.", a, b, result, expected);
            }
        }
        return errors;
    }

    private static int testHashCode() {
        int errors = 0;
        errors += (Objects.hashCode(null) == 0) ? 0 : 1;
        String s = "42";
        errors += (Objects.hashCode(s) == s.hashCode()) ? 0 : 1;
        return errors;
    }

    private static int testHash() {
        int errors = 0;
        Object[] data = new String[] { "perfect", "ham", "THC" };
        errors += ((Objects.hash((Object[]) null) == 0) ? 0 : 1);
        errors += (Objects.hash("perfect", "ham", "THC") == Arrays.hashCode(data)) ? 0 : 1;
        return errors;
    }

    private static int testToString() {
        int errors = 0;
        errors += ("null".equals(Objects.toString(null))) ? 0 : 1;
        String s = "Some string";
        errors += (s.equals(Objects.toString(s))) ? 0 : 1;
        return errors;
    }

    private static int testToString2() {
        int errors = 0;
        String s = "not the default";
        errors += (s.equals(Objects.toString(null, s))) ? 0 : 1;
        errors += (s.equals(Objects.toString(s, "another string"))) ? 0 : 1;
        return errors;
    }

    private static int testCompare() {
        int errors = 0;
        String[] values = { "e. e. cummings", "zzz" };
        String[] VALUES = { "E. E. Cummings", "ZZZ" };
        errors += compareTest(null, null, 0);
        for (int i = 0; i < values.length; i++) {
            String a = values[i];
            errors += compareTest(a, a, 0);
            for (int j = 0; j < VALUES.length; j++) {
                int expected = Integer.compare(i, j);
                String b = VALUES[j];
                errors += compareTest(a, b, expected);
            }
        }
        return errors;
    }

    private static int compareTest(String a, String b, int expected) {
        int errors = 0;
        int result = Objects.compare(a, b, String.CASE_INSENSITIVE_ORDER);
        if (Integer.signum(result) != Integer.signum(expected)) {
            errors++;
            System.err.printf("When comparing %s to %s, got %d instead of %d%n.", a, b, result, expected);
        }
        return errors;
    }

    private static int testRequireNonNull() {
        int errors = 0;
        final String RNN_1 = "1-arg requireNonNull";
        final String RNN_2 = "2-arg requireNonNull";
        final String RNN_3 = "Supplier requireNonNull";
        Function<String, String> rnn1 = s -> Objects.requireNonNull(s);
        Function<String, String> rnn2 = s -> Objects.requireNonNull(s, "trousers");
        Function<String, String> rnn3 = s -> Objects.requireNonNull(s, () -> "trousers");
        errors += testRNN_NonNull(rnn1, RNN_1);
        errors += testRNN_NonNull(rnn2, RNN_2);
        errors += testRNN_NonNull(rnn3, RNN_3);
        errors += testRNN_Null(rnn1, RNN_1, null);
        errors += testRNN_Null(rnn2, RNN_2, "trousers");
        errors += testRNN_Null(rnn3, RNN_3, "trousers");
        return errors;
    }

    private static int testRNN_NonNull(Function<String, String> testFunc, String testFuncName) {
        int errors = 0;
        try {
            String s = testFunc.apply("pants");
            if (s != "pants") {
                System.err.printf(testFuncName + " failed to return its arg");
                errors++;
            }
        } catch (NullPointerException e) {
            System.err.printf(testFuncName + " threw unexpected NPE");
            errors++;
        }
        return errors;
    }

    private static int testRNN_Null(Function<String, String> testFunc, String testFuncName, String expectedMessage) {
        int errors = 0;
        try {
            String s = testFunc.apply(null);
            System.err.printf(testFuncName + " failed to throw NPE");
            errors++;
        } catch (NullPointerException e) {
            if (e.getMessage() != expectedMessage) {
                System.err.printf(testFuncName + " threw NPE w/ bad detail msg");
                errors++;
            }
        }
        return errors;
    }

    private static int testIsNull() {
        int errors = 0;
        errors += Objects.isNull(null) ? 0 : 1;
        errors += Objects.isNull(Objects.class) ? 1 : 0;
        return errors;
    }

    private static int testNonNull() {
        int errors = 0;
        errors += Objects.nonNull(null) ? 1 : 0;
        errors += Objects.nonNull(Objects.class) ? 0 : 1;
        return errors;
    }

    private static int testNonNullOf() {
        int errors = 0;
        String defString = new String("default");
        String nullString = null;
        String nonNullString = "non-null";
        String result = Objects.requireNonNullElse(nullString, defString);
        errors += (result == defString) ? 0 : 1;
        errors += (Objects.requireNonNullElse(nonNullString, defString) == nonNullString) ? 0 : 1;
        errors += (Objects.requireNonNullElse(nonNullString, null) == nonNullString) ? 0 : 1;
        try {
            Objects.requireNonNullElse(null, null);
            errors += 1;
        } catch (NullPointerException npe) {
            errors += npe.getMessage().equals("defaultObj") ? 0 : 1;
        }
        errors += (Objects.requireNonNullElseGet(nullString, () -> defString) == defString) ? 0 : 1;
        errors += (Objects.requireNonNullElseGet(nonNullString, () -> defString) == nonNullString) ? 0 : 1;
        errors += (Objects.requireNonNullElseGet(nonNullString, () -> null) == nonNullString) ? 0 : 1;
        try {
            Objects.requireNonNullElseGet(null, () -> null);
            errors += 1;
        } catch (NullPointerException npe) {
            errors += npe.getMessage().equals("supplier.get()") ? 0 : 1;
        }
        try {
            Objects.requireNonNullElseGet(null, null);
            errors += 1;
        } catch (NullPointerException npe) {
            errors += npe.getMessage().equals("supplier") ? 0 : 1;
        }
        return errors;
    }
}
