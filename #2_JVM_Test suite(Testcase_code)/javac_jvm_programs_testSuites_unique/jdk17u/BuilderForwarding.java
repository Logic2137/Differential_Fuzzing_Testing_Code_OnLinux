import java.util.ArrayList;
import java.util.List;

public class BuilderForwarding {

    private static final String A_STRING_BUFFER_VAL = "aStringBuffer";

    private static final String A_STRING_BUILDER_VAL = "aStringBuilder";

    private static final String A_STRING_VAL = "aString";

    private static final String NON_EMPTY_VAL = "NonEmpty";

    public BuilderForwarding() {
        System.out.println("Starting BuilderForwarding");
    }

    public static void main(String... args) {
        new BuilderForwarding().executeTestMethods();
    }

    public void executeTestMethods() {
        appendCharSequence();
        indexOfString();
        indexOfStringIntNull();
        indexOfStringNull();
        indexOfStringint();
        insertintCharSequence();
        insertintObject();
        insertintboolean();
        insertintchar();
        insertintdouble();
        insertintfloat();
        insertintint();
        insertintlong();
        lastIndexOfString();
        lastIndexOfStringint();
    }

    public void appendCharSequence() {
        CharSequence aString = A_STRING_VAL;
        CharSequence aStringBuilder = new StringBuilder(A_STRING_BUILDER_VAL);
        CharSequence aStringBuffer = new StringBuffer(A_STRING_BUFFER_VAL);
        assertEquals(new StringBuilder().append(aString).toString(), A_STRING_VAL);
        assertEquals(new StringBuilder().append(aStringBuilder).toString(), A_STRING_BUILDER_VAL);
        assertEquals(new StringBuilder().append(aStringBuffer).toString(), A_STRING_BUFFER_VAL);
        assertEquals(new StringBuilder(NON_EMPTY_VAL).append(aString).toString(), NON_EMPTY_VAL + A_STRING_VAL);
        assertEquals(new StringBuilder(NON_EMPTY_VAL).append(aStringBuilder).toString(), NON_EMPTY_VAL + A_STRING_BUILDER_VAL);
        assertEquals(new StringBuilder(NON_EMPTY_VAL).append(aStringBuffer).toString(), NON_EMPTY_VAL + A_STRING_BUFFER_VAL);
    }

    public void indexOfString() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.indexOf(null);
            throw new RuntimeException("Test failed: should have thrown NPE");
        } catch (NullPointerException npe) {
        } catch (Throwable t) {
            throw new RuntimeException("Test failed: should have thrown NPE. Instead threw " + t);
        }
        sb = new StringBuilder("xyz");
        assertEquals(sb.indexOf("y"), 1);
        assertEquals(sb.indexOf("not found"), -1);
    }

    public void indexOfStringint() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.indexOf(null, 1);
            throw new RuntimeException("Test failed: should have thrown NPE");
        } catch (NullPointerException npe) {
        } catch (Throwable t) {
            throw new RuntimeException("Test failed: should have thrown NPE");
        }
        sb = new StringBuilder("xyyz");
        assertEquals(sb.indexOf("y", 0), 1);
        assertEquals(sb.indexOf("y", 1), 1);
        assertEquals(sb.indexOf("y", 2), 2);
        assertEquals(sb.indexOf("not found"), -1);
    }

    public void indexOfStringIntNull() {
        StringBuffer sb = new StringBuffer();
        try {
            sb.indexOf(null, 1);
            throw new RuntimeException("Test failed: should have thrown NPE");
        } catch (NullPointerException npe) {
        } catch (Throwable t) {
            throw new RuntimeException("Test failed: should have thrown NPE. Instead threw " + t);
        }
    }

    public void indexOfStringNull() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.indexOf(null);
            throw new RuntimeException("Test failed: should have thrown NPE");
        } catch (NullPointerException npe) {
        } catch (Throwable t) {
            throw new RuntimeException("Test failed: should have thrown NPE. Instead threw " + t);
        }
    }

    public void insertintboolean() {
        boolean b = true;
        StringBuilder sb = new StringBuilder("012345");
        assertEquals(sb.insert(2, b).toString(), "01true2345");
    }

    public void insertintchar() {
        char c = 'C';
        StringBuilder sb = new StringBuilder("012345");
        assertEquals(sb.insert(2, c).toString(), "01C2345");
    }

    public void insertintCharSequence() {
        final String initString = "012345";
        CharSequence aString = A_STRING_VAL;
        CharSequence aStringBuilder = new StringBuilder(A_STRING_BUILDER_VAL);
        CharSequence aStringBuffer = new StringBuffer(A_STRING_BUFFER_VAL);
        assertEquals(new StringBuilder(initString).insert(2, aString).toString(), "01" + A_STRING_VAL + "2345");
        assertEquals(new StringBuilder(initString).insert(2, aStringBuilder).toString(), "01" + A_STRING_BUILDER_VAL + "2345");
        assertEquals(new StringBuilder(initString).insert(2, aStringBuffer).toString(), "01" + A_STRING_BUFFER_VAL + "2345");
        try {
            new StringBuilder(initString).insert(7, aString);
            throw new RuntimeException("Test failed: should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException soob) {
        } catch (Throwable t) {
            throw new RuntimeException("Test failed: should have thrown IndexOutOfBoundsException, but instead threw " + t.getMessage());
        }
    }

    public void insertintdouble() {
        double d = 99d;
        StringBuilder sb = new StringBuilder("012345");
        assertEquals(sb.insert(2, d).toString(), "0199.02345");
    }

    public void insertintfloat() {
        float f = 99.0f;
        StringBuilder sb = new StringBuilder("012345");
        assertEquals(sb.insert(2, f).toString(), "0199.02345");
    }

    public void insertintint() {
        int i = 99;
        StringBuilder sb = new StringBuilder("012345");
        assertEquals(sb.insert(2, i).toString(), "01992345");
    }

    public void insertintlong() {
        long l = 99;
        StringBuilder sb = new StringBuilder("012345");
        assertEquals(sb.insert(2, l).toString(), "01992345");
    }

    public void insertintObject() {
        StringBuilder sb = new StringBuilder("012345");
        List<String> ls = new ArrayList<String>();
        ls.add("A");
        ls.add("B");
        String lsString = ls.toString();
        assertEquals(sb.insert(2, ls).toString(), "01" + lsString + "2345");
        try {
            sb.insert(sb.length() + 1, ls);
            throw new RuntimeException("Test failed: should have thrown StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException soob) {
        } catch (Throwable t) {
            throw new RuntimeException("Test failed: should have thrown StringIndexOutOfBoundsException, but instead threw:" + t);
        }
    }

    public void lastIndexOfString() {
        String xyz = "xyz";
        String xyz3 = "xyzxyzxyz";
        StringBuilder sb = new StringBuilder(xyz3);
        int pos = sb.lastIndexOf("xyz");
        assertEquals(pos, 2 * xyz.length());
    }

    public void lastIndexOfStringint() {
        StringBuilder sb = new StringBuilder("xyzxyzxyz");
        int pos = sb.lastIndexOf("xyz", 5);
        assertEquals(pos, 3);
        pos = sb.lastIndexOf("xyz", 6);
        assertEquals(pos, 6);
    }

    public void assertEquals(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new RuntimeException("Test failed: actual = '" + actual + "', expected = '" + expected + "'");
        }
    }

    public void assertEquals(int actual, int expected) {
        if (actual != expected) {
            throw new RuntimeException("Test failed: actual = '" + actual + "', expected = '" + expected + "'");
        }
    }
}
