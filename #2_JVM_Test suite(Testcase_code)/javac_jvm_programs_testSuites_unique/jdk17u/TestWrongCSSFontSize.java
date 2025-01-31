import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.GlyphView;
import javax.swing.text.View;

public class TestWrongCSSFontSize {

    private static final String TEXT = "<html><head><style>" + "body { font-size: 14 }" + "div span { font-size: 150% }" + "span { font-size: 200% }" + "h2, .h2 { font-size: 150% }" + "</style></head><body>" + "<h2>Foo</h2>" + "<div class=h2>Bar</div>" + "<ol class=h2><li>Baz</li></ol>" + "<table class=h2><tr><td>Qux</td></tr></table>" + "<table><thead class=h2><tr><th>Qux</th></tr></thead></table>" + "<table><tr class=h2><td>Qux</td></tr></table>" + "<table><tr><td class=h2>Qux</td></tr></table>" + "<div><span>Quux</span></div>" + "</body></html>";

    private static final int expectedFontSize = 21;

    private static final int expectedAssertions = 8;

    private final boolean w3cUnits;

    private JEditorPane editor;

    public TestWrongCSSFontSize(boolean w3cUnits) {
        this.w3cUnits = w3cUnits;
    }

    public void setUp() {
        editor = new JEditorPane();
        editor.setContentType("text/html");
        if (w3cUnits) {
            editor.putClientProperty(JEditorPane.W3C_LENGTH_UNITS, Boolean.TRUE);
        }
        editor.setText(TEXT);
        editor.setSize(editor.getPreferredSize());
    }

    public void run() {
        int count = forEachTextRun(editor.getUI().getRootView(editor), this::assertFontSize);
        if (count != expectedAssertions) {
            throw new AssertionError((w3cUnits ? "w3cUnits - " : "") + "assertion count expected [" + expectedAssertions + "] but found [" + count + "]");
        }
    }

    private int forEachTextRun(View view, Consumer<GlyphView> action) {
        int tested = 0;
        for (int i = 0; i < view.getViewCount(); i++) {
            View child = view.getView(i);
            if (child instanceof GlyphView) {
                if (child.getElement().getAttributes().getAttribute("CR") == Boolean.TRUE) {
                    continue;
                }
                action.accept((GlyphView) child);
                tested += 1;
            } else {
                tested += forEachTextRun(child, action);
            }
        }
        return tested;
    }

    private void assertFontSize(GlyphView child) {
        printSource(child);
        int actualFontSize = child.getFont().getSize();
        if (actualFontSize != expectedFontSize) {
            throw new AssertionError((w3cUnits ? "w3cUnits - " : "") + "font size expected [" + expectedFontSize + "] but found [" + actualFontSize + "]");
        }
    }

    private void printSource(View textRun) {
        try {
            editor.getEditorKit().write(System.out, editor.getDocument(), textRun.getStartOffset(), textRun.getEndOffset() - textRun.getStartOffset());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void captureImage(Component comp, String suffix) {
        try {
            BufferedImage capture = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = capture.getGraphics();
            comp.paint(g);
            g.dispose();
            ImageIO.write(capture, "png", new File(TestWrongCSSFontSize.class.getSimpleName() + suffix + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        TestWrongCSSFontSize test = new TestWrongCSSFontSize(argW3CUnits(args));
        AtomicReference<Throwable> failure = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            try {
                test.setUp();
                test.run();
            } catch (Throwable e) {
                failure.set(e);
            } finally {
                String suffix = test.w3cUnits ? "-w3cUnits" : "";
                if (failure.get() != null) {
                    captureImage(test.editor, suffix + "-failure");
                } else if (argCapture(args)) {
                    captureImage(test.editor, suffix + "-success");
                }
            }
        });
        if (failure.get() != null) {
            throw failure.get();
        }
    }

    private static boolean argW3CUnits(String[] args) {
        return Arrays.asList(args).contains("-w3cUnits");
    }

    private static boolean argCapture(String[] args) {
        return Arrays.asList(args).contains("-capture");
    }
}
