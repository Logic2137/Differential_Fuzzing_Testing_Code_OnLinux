import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;

public class MyanmarTextTest {

    private static final String TEXT = "\u1000\u103C";

    private static final String FONT_WINDOWS = "Myanmar Text";

    private static final String FONT_LINUX = "Padauk";

    private static final String FONT_MACOS = "Myanmar MN";

    private static final String FONT_NAME = selectFontName();

    private final JFrame frame;

    private final JTextField myanmarTF;

    private static volatile MyanmarTextTest mtt;

    public static void main(String[] args) throws Exception {
        if (FONT_NAME == null) {
            System.err.println("Unsupported OS: exiting");
            return;
        }
        if (!fontExists()) {
            System.err.println("Required font is not installed: " + FONT_NAME);
            return;
        }
        try {
            SwingUtilities.invokeAndWait(MyanmarTextTest::createUI);
            SwingUtilities.invokeAndWait(mtt::checkPositions);
        } finally {
            SwingUtilities.invokeAndWait(mtt::dispose);
        }
    }

    private static void createUI() {
        mtt = new MyanmarTextTest();
        mtt.show();
    }

    private MyanmarTextTest() {
        frame = new JFrame("Myanmar Text");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        myanmarTF = new JTextField(TEXT);
        myanmarTF.setFont(new Font(FONT_NAME, Font.PLAIN, 40));
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(myanmarTF);
        main.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        frame.getContentPane().add(main);
    }

    private void show() {
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private void dispose() {
        frame.dispose();
    }

    private void checkPositions() {
        final TextUI ui = myanmarTF.getUI();
        final Position.Bias[] biasRet = new Position.Bias[1];
        try {
            if (2 != ui.getNextVisualPositionFrom(myanmarTF, 0, Position.Bias.Forward, SwingConstants.EAST, biasRet)) {
                throw new RuntimeException("For 0, next position should be 2");
            }
            if (2 != ui.getNextVisualPositionFrom(myanmarTF, 1, Position.Bias.Forward, SwingConstants.EAST, biasRet)) {
                throw new RuntimeException("For 1, next position should be 2");
            }
            if (0 != ui.getNextVisualPositionFrom(myanmarTF, 2, Position.Bias.Forward, SwingConstants.WEST, biasRet)) {
                throw new RuntimeException("For 2, prev position should be 0");
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private static String selectFontName() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            return FONT_WINDOWS;
        } else if (osName.contains("linux")) {
            return FONT_LINUX;
        } else if (osName.contains("mac")) {
            return FONT_MACOS;
        } else {
            return null;
        }
    }

    private static boolean fontExists() {
        String[] fontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        return Arrays.asList(fontFamilyNames).contains(FONT_NAME);
    }
}
