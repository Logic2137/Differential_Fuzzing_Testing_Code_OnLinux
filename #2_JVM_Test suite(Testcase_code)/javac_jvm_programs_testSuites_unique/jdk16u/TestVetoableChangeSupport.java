



import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

public class TestVetoableChangeSupport implements VetoableChangeListener {
    private static final String NAME = "property";

    public static void main(String[] args) throws PropertyVetoException {
        for (int i = 1; i <= 3; i++) {
            test(i, 1, 10000000);
            test(i, 10, 1000000);
            test(i, 100, 100000);
            test(i, 1000, 10000);
            test(i, 10000, 1000);
            test(i, 20000, 1000);
        }
    }

    private static void test(int step, int listeners, int attempts) throws PropertyVetoException {
        TestVetoableChangeSupport test = new TestVetoableChangeSupport();
        VetoableChangeSupport vcs = new VetoableChangeSupport(test);
        PropertyChangeEvent eventNull = new PropertyChangeEvent(test, null, null, null);
        PropertyChangeEvent eventName = new PropertyChangeEvent(test, NAME, null, null);
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < listeners; i++) {
            vcs.addVetoableChangeListener(test);
            vcs.addVetoableChangeListener(NAME, test);
        }
        long time2 = System.currentTimeMillis();
        for (int i = 0; i < attempts; i++) {
            vcs.fireVetoableChange(eventNull);
            vcs.fireVetoableChange(eventName);
        }
        long time3 = System.currentTimeMillis();
        time1 = time2 - time1; 
        time2 = time3 - time2; 
        System.out.println("Step: " + step
                        + "; Listeners: " + listeners
                        + "; Attempts: " + attempts
                        + "; Time (ms): " + time1 + "/" + time2);
    }

    public void vetoableChange(PropertyChangeEvent event) {
    }
}
