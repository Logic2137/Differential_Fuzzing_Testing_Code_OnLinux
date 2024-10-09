

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import static javax.swing.UIManager.getInstalledLookAndFeels;


public final class RootPaneDecorationSize {

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            EventQueue.invokeAndWait(() -> setLookAndFeel(laf));
            EventQueue.invokeAndWait(RootPaneDecorationSize::test);
        }
    }

    private static void test() {
        JFrame frame = new JFrame();
        Dimension size;
        Dimension min;
        Dimension pref;
        try {
            
            frame.setUndecorated(true);
            frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
            
            JLayeredPane layeredPane = frame.getRootPane().getLayeredPane();
            for (Component comp : layeredPane.getComponents()) {
                comp.setMinimumSize(new Dimension(1000, 10));
                comp.setMaximumSize(new Dimension(1000, 10));
                comp.setPreferredSize(new Dimension(1000, 10));
            }
            frame.pack();
            size = frame.getSize();
            min = frame.getMinimumSize();
            pref = frame.getPreferredSize();
        } finally {
            frame.dispose();
        }
        System.err.println("\tsize = " + size);
        System.err.println("\tminimumSize = " + min);
        System.err.println("\tpreferredSize = " + pref);

        
        
        if (size.height > 1000 || min.height > 1000 || pref.height > 1000) {
            throw new RuntimeException("The height too big");
        }
        if (size.width < 1000 || min.width < 1000 || pref.width < 1000) {
            throw new RuntimeException("The width too small");
        }
    }

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            System.err.println("LookAndFeel: " + laf.getClassName());
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored){
            System.err.println("Unsupported LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
