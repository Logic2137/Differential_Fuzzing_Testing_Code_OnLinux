import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

public final class TestEquals implements VetoableChangeListener {

    private static final String PROPERTY = "property";

    public static void main(String[] args) throws PropertyVetoException {
        TestEquals one = new TestEquals(1);
        TestEquals two = new TestEquals(2);
        Object source = TestEquals.class;
        VetoableChangeSupport vcs = new VetoableChangeSupport(source);
        vcs.addVetoableChangeListener(PROPERTY, one);
        vcs.addVetoableChangeListener(PROPERTY, two);
        PropertyChangeEvent event = new PropertyChangeEvent(source, PROPERTY, one, two);
        vcs.fireVetoableChange(event);
        test(one, two, 1);
        vcs.fireVetoableChange(PROPERTY, one, two);
        test(one, two, 2);
    }

    private static void test(TestEquals v1, TestEquals v2, int amount) {
        int count = v1.count + v2.count;
        if (amount < count)
            throw new Error("method equals() is called " + count + " times");
        v1.count = 0;
        v2.count = 0;
    }

    private final int value;

    private int count;

    private TestEquals(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TestEquals) {
            this.count++;
            TestEquals that = (TestEquals) object;
            return that.value == this.value;
        }
        return false;
    }

    public void vetoableChange(PropertyChangeEvent event) {
    }
}
