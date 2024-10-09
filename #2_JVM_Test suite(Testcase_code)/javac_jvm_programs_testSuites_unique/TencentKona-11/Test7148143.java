



import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.EventListener;
import java.util.EventListenerProxy;

public class Test7148143 {

    private static class CustomProxy
            extends EventListenerProxy<EventListener>
            implements VetoableChangeListener {

        public CustomProxy() {
            super(new EventListener() {
            });
        }

        public void vetoableChange(PropertyChangeEvent event) {
        }
    }

    public static void main(String[] args) {
        VetoableChangeListener listener = new CustomProxy();
        VetoableChangeSupport support = new VetoableChangeSupport(listener);
        support.addVetoableChangeListener(listener);
        support.addVetoableChangeListener("foo", listener); 
    }
}
