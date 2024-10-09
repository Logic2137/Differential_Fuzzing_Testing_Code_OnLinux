import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Pending implements Runnable {

    private static volatile boolean passed;

    public static void main(final String[] args) throws Exception {
        SwingUtilities.invokeLater(new Pending());
        Thread.sleep(10000);
        if (!passed) {
            throw new RuntimeException("Test failed");
        }
    }

    @Override
    public void run() {
        UIManager.put("foobar", "Pending");
        UIManager.get("foobar");
        passed = true;
    }
}
