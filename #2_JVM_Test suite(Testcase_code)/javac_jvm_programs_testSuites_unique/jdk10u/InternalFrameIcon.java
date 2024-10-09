

import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import static javax.swing.UIManager.getInstalledLookAndFeels;


public final class InternalFrameIcon implements Runnable {

    public static void main(final String[] args) throws Exception {
        for (final UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
            SwingUtilities.invokeAndWait(new InternalFrameIcon());
        }
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
            System.out.println("LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Object o = UIManager.getDefaults().get("InternalFrame.icon");
        if (o != null && !(o instanceof Icon)) {
            throw new RuntimeException("Wrong object: " + o);
        }
    }
}
