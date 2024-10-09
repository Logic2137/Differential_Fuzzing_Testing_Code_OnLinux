

import java.awt.*;
import java.awt.event.*;



public final class ScrollbarMouseWheelTest
        implements MouseWheelListener, WindowListener {

    final static String tk = Toolkit.getDefaultToolkit().getClass().getName();
    final static int REPS = 5;
    
    
    
    
    
    final static int PANEL_REPS = tk.equals("sun.awt.windows.WToolkit")? REPS * 2: REPS;

    Scrollbar sb1;
    Scrollbar sb2;
    Panel pnl;
    class Sema {
        boolean flag;
        boolean getVal() { return flag;}
        void setVal(boolean b) { flag = b;}
    }
    Sema sema = new Sema();

    Robot robot;

    int sb1upevents, sb2upevents, pnlupevents;
    int sb1downevents, sb2downevents, pnldownevents;

    public static void main(final String[] args) {
        new ScrollbarMouseWheelTest().test();
    }

    public void test() {
        
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.out.println("Problem creating Robot.  FAIL.");
            throw new RuntimeException("Problem creating Robot.  FAIL.");

        }

        robot.setAutoDelay(500);
        robot.setAutoWaitForIdle(true);

        
        Frame frame = new Frame("ScrollbarMouseWheelTest");
        frame.addWindowListener(this);
        pnl = new Panel();
        pnl.setLayout(new GridLayout(1, 2));
        pnl.addMouseWheelListener(this);
        sb1 = new Scrollbar();
        sb1.addMouseWheelListener(this);
        pnl.add(sb1);
        sb2 = new Scrollbar();
        pnl.add(sb2);
        frame.add(pnl);
        frame.setSize(200, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.toFront();

        
        while (true) {
            synchronized (sema) {
                if (sema.getVal()) {
                    break;
                }
            }
        }
        
        testComp(sb1, true);
        
        testComp(sb1, false);
        
        testComp(sb2, true);
        
        testComp(sb2, false);
        frame.dispose();
        System.out.println("Test done.");
        if (sb1upevents == REPS &&
                sb2upevents == 0 &&
                pnlupevents == PANEL_REPS &&
                sb1downevents == REPS &&
                sb2downevents == 0 &&
                pnldownevents == PANEL_REPS) {
            System.out.println("PASSED.");
        } else {
            System.out.println("Test Failed:" +
                                       "\n\tsb1upevents =" + sb1upevents +
                                       "\n\tsb2upevents = " + sb2upevents +
                                       "\n\tpnlupevents = " + pnlupevents +
                                       "\n\tsb1downevents =" + sb1downevents +
                                       "\n\tsb2downevents = " + sb2downevents +
                                       "\n\tpnldownevents = " + pnldownevents);
            throw new RuntimeException("Test FAILED.");
        }
    }

    public void testComp(Component comp, boolean up) {
        Point loc = comp.getLocationOnScreen();
        robot.mouseMove(loc.x + comp.getWidth() / 2,
                        loc.y + comp.getHeight() / 2);
        for (int loop = 0; loop < REPS; loop++) {
            System.out.println("Robot.mouseWheel() on " + comp.getName());
            robot.mouseWheel(up ? -1 : 1);
        }
    }

    public void mouseWheelMoved(MouseWheelEvent mwe) {
        Component src = mwe.getComponent();
        System.out.println("mouseWheelMoved() on " + src.getName());
        if (mwe.getWheelRotation() == -1) {
            if (src == sb1) {
                sb1upevents++;
            } else if (src == sb2) {
                sb2upevents++;
            } else if (src == pnl) {
                pnlupevents++;
            } else {
                System.out.println("weird source component");
            }
        } else if (mwe.getWheelRotation() == 1) {
            if (src == sb1) {
                sb1downevents++;
            } else if (src == sb2) {
                sb2downevents++;
            } else if (src == pnl) {
                pnldownevents++;
            } else {
                System.out.println("weird source component");
            }
        } else {
            System.out.println("weird wheel rotation");
        }
    }

    public void windowActivated(WindowEvent we) {
        synchronized (sema) {
            sema.setVal(true);
        }
    }

    public void windowClosed(WindowEvent we) {}
    public void windowClosing(WindowEvent we) {}
    public void windowDeactivated(WindowEvent we) {}
    public void windowDeiconified(WindowEvent we) {}
    public void windowIconified(WindowEvent we) {}
    public void windowOpened(WindowEvent we) {}
}
