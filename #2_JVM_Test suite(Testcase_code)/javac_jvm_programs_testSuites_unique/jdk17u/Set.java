import java.lang.reflect.*;

public class Set {

    public static void main(String[] argv) throws Throwable {
        boolean failed = false;
        Test t = new Test();
        if (!t.testPrimitive()) {
            failed = true;
            System.out.println("FAILED: testPrimitive()");
        }
        if (!t.testAccessiblePrimitive()) {
            failed = true;
            System.out.println("FAILED: testAccessiblePrimitive()");
        }
        if (!t.testVolatilePrimitive()) {
            failed = true;
            System.out.println("FAILED: testVolatilePrimitive()");
        }
        if (!t.testStaticPrimitive()) {
            failed = true;
            System.out.println("FAILED: testStaticPrimitive()");
        }
        if (!t.testAccessibleStaticPrimitive()) {
            failed = true;
            System.out.println("FAILED: testAccessibleStaticPrimitive()");
        }
        if (!t.testObject()) {
            failed = true;
            System.out.println("FAILED: testObject()");
        }
        if (!t.testAccessibleObject()) {
            failed = true;
            System.out.println("FAILED: testAccessibleObject()");
        }
        if (!t.testVolatileObject()) {
            failed = true;
            System.out.println("FAILED: testVolatileObject()");
        }
        if (!t.testStaticObject()) {
            failed = true;
            System.out.println("FAILED: testStaticObject()");
        }
        if (!t.testAccessibleStaticObject()) {
            failed = true;
            System.out.println("FAILED: testAccessibleStaticObject()");
        }
        if (failed) {
            throw (new Throwable("Test for Field.set FAILED"));
        }
    }
}

class Test {

    private final int i;

    private final Object o;

    public final int ni;

    public final Object no;

    public volatile int vi;

    public volatile Object vo;

    private static final int si = 408 - 343 - 1407;

    private static final Object so = new Object();

    Test() {
        i = 911;
        ni = i;
        vi = i;
        o = new Object();
        no = o;
        vo = o;
    }

    boolean testPrimitive() throws NoSuchFieldException {
        try {
            Field f = this.getClass().getDeclaredField("ni");
            f.setInt(this, 7);
            if (ni != 7) {
                System.out.println("setInt() did not work");
            }
            return false;
        } catch (IllegalAccessException iae) {
            return true;
        }
    }

    boolean testStaticPrimitive() throws NoSuchFieldException {
        try {
            Field f = this.getClass().getDeclaredField("si");
            f.setInt(this, 13);
            if (si != 13) {
                System.out.println("setInt() did not work for static");
            }
            return false;
        } catch (IllegalAccessException iae) {
            return true;
        }
    }

    boolean testObject() throws NoSuchFieldException {
        Object saved = no;
        try {
            Field f = this.getClass().getDeclaredField("no");
            f.set(this, new Object());
            if (no == saved) {
                System.out.println("set() did not work");
            }
            return false;
        } catch (IllegalAccessException iae) {
            return true;
        }
    }

    boolean testStaticObject() throws NoSuchFieldException {
        Object saved = so;
        try {
            Field f = this.getClass().getDeclaredField("so");
            f.set(this, new Object());
            if (so == saved) {
                System.out.println("set() did not work for static");
            }
            return false;
        } catch (IllegalAccessException iae) {
            return true;
        }
    }

    boolean testAccessiblePrimitive() throws NoSuchFieldException {
        try {
            Field f = this.getClass().getDeclaredField("i");
            f.setAccessible(true);
            f.setInt(this, 7);
            if (i != 7) {
                System.out.println("setInt() did not work");
            }
            return true;
        } catch (IllegalAccessException iae) {
            return false;
        }
    }

    boolean testAccessibleStaticPrimitive() throws NoSuchFieldException {
        try {
            Field f = this.getClass().getDeclaredField("si");
            f.setAccessible(true);
            f.setInt(this, 13);
            if (si != 13) {
                System.out.println("setInt() did not work for static");
            }
            return false;
        } catch (IllegalAccessException iae) {
            return true;
        }
    }

    boolean testAccessibleObject() throws NoSuchFieldException {
        Object saved = o;
        try {
            Field f = this.getClass().getDeclaredField("o");
            f.setAccessible(true);
            f.set(this, new Object());
            if (o == saved) {
                System.out.println("set() did not work");
            }
            return true;
        } catch (IllegalAccessException iae) {
            return false;
        }
    }

    boolean testAccessibleStaticObject() throws NoSuchFieldException {
        Object saved = so;
        try {
            Field f = this.getClass().getDeclaredField("so");
            f.setAccessible(true);
            f.set(this, new Object());
            if (so == saved) {
                System.out.println("set() did not work for static");
            }
            return false;
        } catch (IllegalAccessException iae) {
            return true;
        }
    }

    boolean testVolatilePrimitive() throws NoSuchFieldException {
        try {
            Field f = this.getClass().getDeclaredField("vi");
            f.setAccessible(true);
            f.setInt(this, 7);
            if (vi != 7) {
                System.out.println("setInt() did not work");
            }
            return true;
        } catch (IllegalAccessException iae) {
            return false;
        }
    }

    boolean testVolatileObject() throws NoSuchFieldException {
        Object saved = vo;
        try {
            Field f = this.getClass().getDeclaredField("vo");
            f.setAccessible(true);
            f.set(this, new Object());
            if (vo == saved) {
                System.out.println("set() did not work");
            }
            return true;
        } catch (IllegalAccessException iae) {
            return false;
        }
    }
}
