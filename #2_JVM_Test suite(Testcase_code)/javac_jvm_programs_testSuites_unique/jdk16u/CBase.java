

import java.beans.BeanProperty;
import java.beans.PropertyChangeListener;


public class CBase {

    private int value;

    @BeanProperty(
            bound        = true,
            expert       = true,
            hidden       = true,
            preferred    = true,
            required     = true,
            visualUpdate = true,
            description  = "BASE",
            enumerationValues = {"javax.swing.SwingConstants.TOP"}
            )
    public void setValue(int v) { value = v; }
    public  int getValue()      { return value; }

    public void addPropertyChangeListener(PropertyChangeListener l)    {}
    public void removePropertyChangeListener(PropertyChangeListener l) {}
}
