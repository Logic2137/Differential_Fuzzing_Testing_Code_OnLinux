import java.awt.Point;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class Test4177735 implements Runnable {

    private static final long DELAY = 1000L;

    public static void main(String[] args) throws Exception {
        int hsvIndex = 0;
        int panelsLength;
        int finalIndex;
        JColorChooser chooser = new JColorChooser();
        AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
        panelsLength = panels.length;
        for (int i = 0; i < panelsLength; i++) {
            if (panels[i].getDisplayName().equals("HSV")) {
                hsvIndex = i;
            }
        }
        finalIndex = Math.min(hsvIndex, panelsLength - 1);
        chooser.setChooserPanels(new AbstractColorChooserPanel[] { panels[finalIndex] });
        JDialog dialog = show(chooser);
        pause(DELAY);
        dialog.dispose();
        pause(DELAY);
        Test4177735 test = new Test4177735();
        SwingUtilities.invokeAndWait(test);
        if (test.count != 0) {
            throw new Error("JColorChooser leaves " + test.count + " threads running");
        }
    }

    static JDialog show(JColorChooser chooser) {
        JDialog dialog = JColorChooser.createDialog(null, null, false, chooser, null, null);
        dialog.setVisible(true);
        Point point = null;
        while (point == null) {
            try {
                point = dialog.getLocationOnScreen();
            } catch (IllegalStateException exception) {
                pause(DELAY);
            }
        }
        return dialog;
    }

    private static void pause(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException exception) {
        }
    }

    private int count;

    public void run() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        Thread[] threads = new Thread[group.activeCount()];
        int count = group.enumerate(threads, false);
        for (int i = 0; i < count; i++) {
            String name = threads[i].getName();
            if ("SyntheticImageGenerator".equals(name)) {
                this.count++;
            }
        }
    }
}
