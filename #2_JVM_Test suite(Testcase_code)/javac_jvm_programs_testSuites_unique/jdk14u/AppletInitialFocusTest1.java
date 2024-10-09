

import java.awt.*;
import java.awt.event.*;


public class AppletInitialFocusTest1 extends Frame implements FocusListener {

    Button button1 = new Button("Button1");
    Button button2 = new Button("Button2");

    Object lock = new Object();

    public static void main(final String[] args) throws Exception {
        AppletInitialFocusTest1 app = new AppletInitialFocusTest1();
        app.init();
        Thread.sleep(10000);
    }

    public void init() {
        setSize(200, 200);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        Component parent = this;
        while (parent != null && !(parent instanceof Window)) {
            parent = parent.getParent();
        }
        
        if (parent == null) {
            synchronized (lock) {
                System.err.println("appletviewer not running");
                System.exit(3);
            }
        }
        button1.addFocusListener(this);
        button2.addFocusListener(this);
        add(button1);
        add(button2);
        setVisible(true);
        button2.requestFocus();
    }

    public void focusGained(FocusEvent e) {
        if (e.getSource() == button1) {
            synchronized (lock) {
                throw new RuntimeException("failed: focus on the wrong button");
            }
        }
    }

    public void focusLost(FocusEvent e) {
    }
}
