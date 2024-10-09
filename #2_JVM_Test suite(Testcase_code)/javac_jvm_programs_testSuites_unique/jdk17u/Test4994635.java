import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeListenerProxy;
import java.beans.VetoableChangeSupport;

public class Test4994635 implements VetoableChangeListener {

    public static void main(String[] args) {
        Test4994635 test = new Test4994635();
        test.vcs.addVetoableChangeListener(new VetoableChangeListenerProxy("property", test));
    }

    private final VetoableChangeSupport vcs = new VetoableChangeSupport(this);

    public void vetoableChange(PropertyChangeEvent event) {
    }
}
