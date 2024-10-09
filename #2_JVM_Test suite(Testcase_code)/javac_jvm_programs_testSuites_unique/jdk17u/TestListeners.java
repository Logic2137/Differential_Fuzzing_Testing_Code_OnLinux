import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeListenerProxy;
import java.beans.VetoableChangeSupport;

public final class TestListeners implements VetoableChangeListener {

    private static final String NAME = "property";

    private static final String NONE = "broken";

    private static int current;

    public static void main(String[] args) throws PropertyVetoException {
        VetoableChangeSupport vcs = new VetoableChangeSupport(TestListeners.class);
        vcs.addVetoableChangeListener(new TestListeners(0));
        vcs.addVetoableChangeListener(NAME, new TestListeners(2));
        vcs.addVetoableChangeListener(new TestListeners(1));
        vcs.addVetoableChangeListener(NAME, new VetoableChangeListenerProxy(NAME, new TestListeners(3)));
        current = 0;
        vcs.fireVetoableChange(NAME, 0, 1);
        if (current != 4)
            throw new Error("Expected 4 listeners, but called " + current);
        current = 0;
        vcs.fireVetoableChange(NONE, 1, 0);
        if (current != 2)
            throw new Error("Expected 2 listeners, but called " + current);
        VetoableChangeListener[] all = vcs.getVetoableChangeListeners();
        if (all.length != 4)
            throw new Error("Expected 4 listeners, but contained " + all.length);
        VetoableChangeListener[] named = vcs.getVetoableChangeListeners(NAME);
        if (named.length != 2)
            throw new Error("Expected 2 named listeners, but contained " + named.length);
        VetoableChangeListener[] other = vcs.getVetoableChangeListeners(NONE);
        if (other.length != 0)
            throw new Error("Expected 0 other listeners, but contained " + other.length);
        vcs.removeVetoableChangeListener(new TestListeners(0));
        vcs.removeVetoableChangeListener(new TestListeners(1));
        vcs.removeVetoableChangeListener(NAME, new TestListeners(2));
        vcs.removeVetoableChangeListener(NAME, new VetoableChangeListenerProxy(NAME, new TestListeners(3)));
        all = vcs.getVetoableChangeListeners();
        if (all.length != 0)
            throw new Error("Expected 4 listeners, but contained " + all.length);
        named = vcs.getVetoableChangeListeners(NAME);
        if (named.length != 0)
            throw new Error("Expected 2 named listeners, but contained " + named.length);
        other = vcs.getVetoableChangeListeners(NONE);
        if (other.length != 0)
            throw new Error("Expected 0 other listeners, but contained " + other.length);
    }

    private final int index;

    public TestListeners(int index) {
        this.index = index;
    }

    public void vetoableChange(PropertyChangeEvent event) {
        if (this.index < 0)
            throw new Error("Unexpected listener: " + this.index);
        System.out.println("index = " + this.index);
        if (this.index != current++)
            throw new Error("Unexpected listener: " + this.index);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TestListeners) {
            TestListeners test = (TestListeners) object;
            return test.index == this.index;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.index;
    }
}
