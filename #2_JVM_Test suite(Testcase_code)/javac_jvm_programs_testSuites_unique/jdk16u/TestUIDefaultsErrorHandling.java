



import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

public class TestUIDefaultsErrorHandling {

    private final static String erroutput = "oops, complex problem with diagnostics";

    public static void main(String[] args) {
        final PrintStream err = System.err;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        System.setErr(new PrintStream(bytes));

        try {
            UIDefaults defaults = UIManager.getDefaults();
            defaults.put("BrokenUI", BrokenUI.class.getName());
            defaults.getUI(new JLabel() {
                public @Override String getUIClassID() {
                    return "BrokenUI";
                }
            });
            if (!(bytes.toString().contains(erroutput))) {
                throw new RuntimeException("UIDefauls swallows exception trace");
            }
        } finally {
            System.setErr(err);
        }
    }
    public static class BrokenUI extends BasicLabelUI {
        public static ComponentUI createUI(JComponent target) {
            return new BrokenUI();
        }
        private BrokenUI() {
            throw new RuntimeException(erroutput);
        }
    }
}
