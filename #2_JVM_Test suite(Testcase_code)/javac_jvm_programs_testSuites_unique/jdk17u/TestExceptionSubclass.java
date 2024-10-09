
package jdk.jfr.event.runtime;

import java.util.concurrent.TimeUnit;

public class TestExceptionSubclass {

    public TestExceptionSubclass() {
        try {
            throw new PerfectlyFineException(TimeUnit.MILLISECONDS);
        } catch (PerfectlyFineException e) {
        }
    }

    public static void main(String[] args) throws Throwable {
        new TestExceptionSubclass();
    }

    class PerfectlyFineException extends Error {

        private static final long serialVersionUID = 1L;

        private final TimeUnit unit;

        PerfectlyFineException(TimeUnit unit) {
            this.unit = unit;
        }

        public String getMessage() {
            return "Failed in " + unit.toNanos(1) + " ns";
        }
    }
}
