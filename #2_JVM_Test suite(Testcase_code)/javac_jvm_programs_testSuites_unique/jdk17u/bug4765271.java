import java.awt.Font;
import java.awt.Shape;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument.AbstractElement;
import javax.swing.text.AttributeSet;
import javax.swing.text.View;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class bug4765271 {

    private static final int RES = 96;

    private static final String TEXT = "<html>" + "<body>" + "<span style=\"font-size: 72pt  \">A</span>" + "<span style=\"font-size: 6pc   \">B</span>" + "<span style=\"font-size: " + RES + "px  \">C</span>" + "<span style=\"font-size: 25.4mm\">D</span>" + "<span style=\"font-size: 2.54cm\">E</span>" + "<span style=\"font-size: 1in   \">F</span>" + "</body>" + "</html>";

    private JEditorPane jep;

    private final boolean showFrame;

    private final AtomicBoolean passed = new AtomicBoolean(true);

    public bug4765271(boolean showFrame) {
        this.showFrame = showFrame;
    }

    public void init() {
        System.out.println("res = " + RES);
        jep = new JEditorPane();
        jep.putClientProperty(JEditorPane.W3C_LENGTH_UNITS, Boolean.TRUE);
        jep.setEditorKit(new HTMLEditorKit());
        jep.setEditable(false);
        jep.setText(TEXT);
        if (showFrame) {
            JFrame f = new JFrame("Reg test for bug4765271");
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(jep);
            f.pack();
            f.setVisible(true);
        } else {
            jep.setSize(jep.getPreferredSize());
        }
    }

    public void test() {
        Shape r = jep.getBounds();
        View v = jep.getUI().getRootView(jep);
        while (!(v instanceof javax.swing.text.html.InlineView)) {
            String viewName = v.getClass().getName();
            int n = v.getViewCount();
            if (viewName.endsWith("Row")) {
                break;
            }
            Shape sh = v.getChildAllocation(n - 1, r);
            if (sh != null) {
                r = sh;
            }
            v = v.getView(n - 1);
        }
        Shape sh = v.getChildAllocation(0, r);
        int h1 = sh.getBounds().height;
        StyleSheet ss = ((HTMLDocument) v.getDocument()).getStyleSheet();
        View childView = v.getView(0);
        AttributeSet attrs = childView.getAttributes();
        Font font = ss.getFont(attrs);
        int size1 = font.getSize();
        System.out.println("Font Size for InlineView #0 = " + size1 + "; height = " + h1 + "; element = {");
        ((AbstractElement) childView.getElement()).dump(System.out, 3);
        System.out.println("}");
        boolean testPassed = true;
        int n = v.getViewCount() - 1;
        for (int i = 1; i < n; i++) {
            sh = v.getChildAllocation(i, r);
            int h2 = sh.getBounds().height;
            childView = v.getView(i);
            attrs = childView.getAttributes();
            font = ss.getFont(attrs);
            int size2 = font.getSize();
            System.out.println("Font Size for InlineView #" + i + " = " + size2 + "; height = " + h2 + "; element = {");
            ((AbstractElement) childView.getElement()).dump(System.out, 3);
            System.out.println("}");
            testPassed &= ((size1 == size2) && (h1 == h2));
        }
        passed.set(testPassed);
    }

    public static void main(String[] args) throws Exception {
        bug4765271 test = new bug4765271((args.length > 0) && "-show".equals(args[0]));
        SwingUtilities.invokeAndWait(() -> {
            test.init();
            test.test();
        });
        if (!test.passed.get()) {
            throw new RuntimeException("Test failed");
        } else {
            System.out.println("Test succeeded");
        }
    }
}
