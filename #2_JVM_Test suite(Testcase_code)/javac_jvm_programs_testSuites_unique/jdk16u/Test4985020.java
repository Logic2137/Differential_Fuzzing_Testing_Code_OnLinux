



import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

public class Test4985020 implements PropertyChangeListener, VetoableChangeListener {
    private static final String NAME = new String("Property Name");
    private static boolean failed;

    public static void main(String[] args) {
        PropertyChangeSupport pcs = new PropertyChangeSupport(args);
        VetoableChangeSupport vcs = new VetoableChangeSupport(args);
        Test4985020 listener = new Test4985020();


        
        System.out.println("PropertyChangeSupport.addPropertyChangeListener(null, listener)");
        try {
            pcs.addPropertyChangeListener(null, listener);
            pcs.firePropertyChange(NAME, null, null);
        }
        catch (Throwable error) {
            print(error);
        }

        
        System.out.println("PropertyChangeSupport.removePropertyChangeListener(null, listener)");
        try {
            pcs.removePropertyChangeListener(null, listener);
            pcs.firePropertyChange(NAME, null, null);
        }
        catch (Throwable error) {
            print(error);
        }

        
        System.out.println("PropertyChangeSupport.getPropertyChangeListeners(null)");
        try {
            PropertyChangeListener[] pcls = pcs.getPropertyChangeListeners(null);
            if (pcls == null) {
                throw new Error("getPropertyChangeListener() returned null");
            }
            if (pcls.length != 0) {
                throw new Error("getPropertyChangeListener() did not return an empty array");
            }
        }
        catch (Throwable error) {
            print(error);
        }

        
        System.out.println("PropertyChangeSupport.hasListeners(null)");
        try {
            pcs.hasListeners(null);
        }
        catch (Throwable error) {
            print(error);
        }

        
        System.out.println("PropertyChangeSupport.hasListeners(null) with a generic listener");
        try {
            pcs.addPropertyChangeListener(listener);
            if (!pcs.hasListeners(null)) {
                throw new Error("hasListeners(null) returned false, but there was a generic listener");
            }
        }
        catch (Throwable error) {
            print(error);
        }

        pcs = new PropertyChangeSupport(args); 

        
        System.out.println("PropertyChangeSupport.hasListeners(null) with a specific listener");
        try {
            pcs.addPropertyChangeListener(NAME, listener);
            if (pcs.hasListeners(null)) {
                throw new Error("hasListeners(null) returned true, but there were no generic listeners - only a specific listener");
            }
        }
        catch (Throwable error) {
            print(error);
        }


        
        System.out.println("VetoableChangeSupport.addVetoableChangeListener(null, listener)");
        try {
            vcs.addVetoableChangeListener(null, listener);
            vcs.fireVetoableChange(NAME, null, null);
        }
        catch (Throwable error) {
            print(error);
        }

        
        System.out.println("VetoableChangeSupport.removeVetoableChangeListener(null, listener)");
        try {
            vcs.removeVetoableChangeListener(null, listener);
            vcs.fireVetoableChange(NAME, null, null);
        }
        catch (Throwable error) {
            print(error);
        }

        
        System.out.println("VetoableChangeSupport.getVetoableChangeListeners(null)");
        try {
            VetoableChangeListener[] pcls = vcs.getVetoableChangeListeners(null);
            if (pcls == null) {
                throw new Error("getVetoableChangeListener() returned null");
            }
            if (pcls.length != 0) {
                throw new Error("getVetoableChangeListener() did not return an empty array");
            }
        }
        catch (Throwable error) {
            print(error);
        }

        
        System.out.println("VetoableChangeSupport.hasListeners(null)");
        try {
            boolean result = vcs.hasListeners(null);
        }
        catch (Throwable error) {
            print(error);
        }

        
        System.out.println("VetoableChangeSupport.hasListeners(null) with a generic listener");
        try {
            vcs.addVetoableChangeListener(listener);
            if (!vcs.hasListeners(null)) {
                throw new Error("hasListeners(null) returned false, but there was a generic listener");
            }
        }
        catch (Throwable error) {
            print(error);
        }

        vcs = new VetoableChangeSupport(args); 

        
        System.out.println("VetoableChangeSupport.hasListeners(null) with a specific listener");
        try {
            vcs.addVetoableChangeListener(NAME, listener);
            if (vcs.hasListeners(null)) {
                throw new Error("hasListeners(null) returned true, but there were no generic listeners - only a specific listener");
            }
        }
        catch (Throwable error) {
            print(error);
        }


        if (failed) {
            throw new Error("TEST FAILED");
        }
    }

    private static void print(Throwable error) {
        error.printStackTrace();
        failed = true;
    }

    public void propertyChange(PropertyChangeEvent event) {
        System.out.println("* propertyChange(event) event is " + event.getPropertyName());
        throw new Error("shouldn't be any listeners");
    }

    public void vetoableChange(PropertyChangeEvent event) {
        System.out.println("* vetoableChange(event) event is " + event.getPropertyName());
        throw new Error("shouldn't be any listeners");
    }
}
