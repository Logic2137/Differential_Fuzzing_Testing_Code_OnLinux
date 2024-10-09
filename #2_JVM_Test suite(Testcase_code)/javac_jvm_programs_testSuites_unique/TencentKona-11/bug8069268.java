



import javax.swing.*;
import java.awt.event.*;
import javax.accessibility.*;

public class bug8069268{
    public static void main(String[] args) throws Exception {
        TestableRootPane rootPane = new TestableRootPane();

        
        
        AccessibleContext acc = rootPane.getAccessibleContext();
        JComponent.AccessibleJComponent accJ = (JComponent.AccessibleJComponent) acc;
        accJ.addPropertyChangeListener(null);

        
        if (!rootPane.testContainerListener())
            throw new RuntimeException("Failed test for bug 8069268");
    }

    private static class TestableRootPane extends JRootPane {
        public boolean testContainerListener() {
            boolean result = false;
            ContainerListener[] listeners = getContainerListeners();
            System.out.println("ContainerListener number is " + listeners.length);
            result = (listeners.length == 1) ? true : false;
            return result;
        }
    }
}
