

package jdk.dynalink.test;

public class ArrayRunnableTest {
    private Runnable[] r;
    public void setRunnables(final Runnable... r) {
        this.r = r;
    }

    public Runnable getFirstRunnable() {
        return r[0];
    }

    public void setRunnablesOverloaded(final Runnable... r) {
        this.r = r;
    }

    public void setRunnablesOverloaded(final Object... r) {
        throw new UnsupportedOperationException();
    }
}
