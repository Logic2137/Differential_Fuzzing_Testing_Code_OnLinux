import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class TestTitledBorderLeak {

    final static int TOTAL_TITLEDBORDER = 10;

    final static int GC_ATTEMPTS = 10;

    static ArrayList<WeakReference<TitledBorder>> weakRefArrTB = new ArrayList(TOTAL_TITLEDBORDER);

    public static void main(String[] args) throws Exception {
        JFrame[] frame = new JFrame[TOTAL_TITLEDBORDER];
        SwingUtilities.invokeAndWait(() -> {
            for (int i = 0; i < TOTAL_TITLEDBORDER; i++) {
                TitledBorder tb = new TitledBorder("");
                weakRefArrTB.add(i, new WeakReference<TitledBorder>(tb));
                JLabel label = new JLabel("TitledBorder");
                label.setBorder(tb);
                frame[i] = new JFrame("Borders");
                JPanel panel = new JPanel();
                panel.add(label);
                frame[i].setContentPane(panel);
                frame[i].setVisible(true);
            }
        });
        if (TOTAL_TITLEDBORDER != weakRefArrTB.size()) {
            System.err.println("TOTAL_TITLEDBORDER != weakRefArrTB.size()");
        }
        Thread.sleep(3000);
        SwingUtilities.invokeAndWait(() -> {
            for (int i = 0; i < TOTAL_TITLEDBORDER; i++) {
                frame[i].dispose();
                frame[i] = null;
            }
        });
        Thread.sleep(3000);
        attemptGCTitledBorder();
        if (TOTAL_TITLEDBORDER != getCleanedUpTitledBorderCount()) {
            throw new RuntimeException("Expected Total TitledBorder to be freed : " + TOTAL_TITLEDBORDER + " Freed " + getCleanedUpTitledBorderCount());
        }
        System.out.println("OK");
    }

    private static void attemptGCTitledBorder() {
        for (int i = 0; i < GC_ATTEMPTS; i++) {
            System.gc();
            System.runFinalization();
            if (getCleanedUpTitledBorderCount() == TOTAL_TITLEDBORDER) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException occurred during Thread.sleep()");
            }
        }
    }

    private static int getCleanedUpTitledBorderCount() {
        int count = 0;
        for (WeakReference<TitledBorder> ref : weakRefArrTB) {
            if (ref.get() == null) {
                count++;
            }
        }
        return count;
    }
}
