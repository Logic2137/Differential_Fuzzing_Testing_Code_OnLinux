
package toolbox;

import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public abstract class TestRunner {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Test {
    }

    int testCount = 0;

    int errorCount = 0;

    public String testName = null;

    protected PrintStream out;

    public TestRunner(PrintStream out) {
        this.out = out;
    }

    protected void runTests() throws Exception {
        runTests(f -> new Object[0]);
    }

    protected void runTests(Function<Method, Object[]> f) throws Exception {
        for (Method m : getClass().getDeclaredMethods()) {
            Annotation a = m.getAnnotation(Test.class);
            if (a != null) {
                testName = m.getName();
                try {
                    testCount++;
                    out.println("test: " + testName);
                    m.invoke(this, f.apply(m));
                } catch (InvocationTargetException e) {
                    errorCount++;
                    Throwable cause = e.getCause();
                    out.println("Exception running test " + testName + ": " + e.getCause());
                    cause.printStackTrace(out);
                }
                out.println();
            }
        }
        if (testCount == 0) {
            throw new Error("no tests found");
        }
        StringBuilder summary = new StringBuilder();
        if (testCount != 1) {
            summary.append(testCount).append(" tests");
        }
        if (errorCount > 0) {
            if (summary.length() > 0) {
                summary.append(", ");
            }
            summary.append(errorCount).append(" errors");
        }
        out.println(summary);
        if (errorCount > 0) {
            throw new Exception(errorCount + " errors found");
        }
    }

    protected void error(String message) {
        out.println("Error: " + message);
        errorCount++;
    }
}
