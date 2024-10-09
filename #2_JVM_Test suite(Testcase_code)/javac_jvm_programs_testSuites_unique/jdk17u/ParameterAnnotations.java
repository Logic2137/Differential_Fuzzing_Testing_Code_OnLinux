import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@interface Named {

    String value();
}

public class ParameterAnnotations {

    static class MyPolicy extends Policy {

        final Policy defaultPolicy;

        MyPolicy(Policy defaultPolicy) {
            this.defaultPolicy = defaultPolicy;
        }

        public boolean implies(ProtectionDomain pd, Permission p) {
            return p.getName().equals("setSecurityManager") || defaultPolicy.implies(pd, p);
        }
    }

    public void nop(@Named("foo") Object foo, @Named("bar") Object bar) {
    }

    void test(String[] args) throws Throwable {
        test1();
        Policy defaultPolicy = Policy.getPolicy();
        Policy.setPolicy(new MyPolicy(defaultPolicy));
        System.setSecurityManager(new SecurityManager());
        try {
            test1();
        } finally {
            System.setSecurityManager(null);
            Policy.setPolicy(defaultPolicy);
        }
    }

    void test1() throws Throwable {
        for (Method m : thisClass.getMethods()) {
            if (m.getName().equals("nop")) {
                Annotation[][] ann = m.getParameterAnnotations();
                equal(ann.length, 2);
                Annotation foo = ann[0][0];
                Annotation bar = ann[1][0];
                equal(foo.toString(), "@Named(\"foo\")");
                equal(bar.toString(), "@Named(\"bar\")");
                check(foo.equals(foo));
                check(!foo.equals(bar));
            }
        }
    }

    volatile int passed = 0, failed = 0;

    void pass() {
        passed++;
    }

    void fail() {
        failed++;
        Thread.dumpStack();
    }

    void fail(String msg) {
        System.err.println(msg);
        fail();
    }

    void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    void check(boolean cond) {
        if (cond)
            pass();
        else
            fail();
    }

    void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y))
            pass();
        else
            fail(x + " not equal to " + y);
    }

    static Class<?> thisClass = new Object() {
    }.getClass().getEnclosingClass();

    public static void main(String[] args) throws Throwable {
        try {
            thisClass.getMethod("instanceMain", String[].class).invoke(thisClass.newInstance(), (Object) args);
        } catch (Throwable e) {
            throw e.getCause();
        }
    }

    public void instanceMain(String[] args) throws Throwable {
        try {
            test(args);
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0)
            throw new AssertionError("Some tests failed");
    }
}
