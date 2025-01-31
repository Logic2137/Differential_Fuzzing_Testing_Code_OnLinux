

import java.util.Map;
import java.util.HashSet;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIDefaults;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.basic.BasicLookAndFeel;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.KEY_TEXT_LCD_CONTRAST;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;


public class bug6302464 {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(bug6302464::testAntialiasingProperties);
    }

    private static void testAntialiasingProperties() {
        testCustomLAF();
        testFontRenderingContext();
        testAntialiasingHints();
        testLAFAAHints();
    }

    private static void testCustomLAF() {
        try {
            testCustomLAF(false);
            testCustomLAF(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testCustomLAF(boolean useAAHints) throws Exception {
        CustomLookAndFeel customLAF = new CustomLookAndFeel(useAAHints);
        UIManager.setLookAndFeel(customLAF);

        JLabel label = new JLabel();
        Object aaHint = label.getClientProperty(KEY_TEXT_ANTIALIASING);
        Object lcdContrastHint = label.getClientProperty(KEY_TEXT_LCD_CONTRAST);

        if (aaHint != customLAF.getAAHint()) {
            throw new RuntimeException("AA hint from custom L&F is not set");
        }

        if (lcdContrastHint != customLAF.getLCDContarstHint()) {
            throw new RuntimeException("AA hint from custom L&F is not set");
        }
    }

    private static final Object[] ANTIALIASING_HINTS = {
        VALUE_TEXT_ANTIALIAS_GASP,
        VALUE_TEXT_ANTIALIAS_LCD_HRGB,
        VALUE_TEXT_ANTIALIAS_LCD_HBGR,
        VALUE_TEXT_ANTIALIAS_LCD_VRGB,
        VALUE_TEXT_ANTIALIAS_LCD_VBGR
    };

    private static void testFontRenderingContext() {
        for (Object aaHint : ANTIALIASING_HINTS) {
            testFontRenderingContext(aaHint);
        }
    }

    private static void testFontRenderingContext(Object aaHint) {

        JLabel label = new JLabel("Test");
        label.putClientProperty(KEY_TEXT_ANTIALIASING, aaHint);
        FontRenderContext frc = label.getFontMetrics(
                label.getFont()).getFontRenderContext();

        if (!aaHint.equals(frc.getAntiAliasingHint())) {
            throw new RuntimeException("Wrong aa hint in FontRenderContext");
        }
    }

    private static void testAntialiasingHints() {
        setMetalLookAndFeel();

        HashSet colorsAAOff = getAntialiasedColors(VALUE_TEXT_ANTIALIAS_OFF, 100);

        if (colorsAAOff.size() > 2) {
            throw new RuntimeException("Wrong number of antialiased colors.");
        }

        HashSet colorsAAOnLCD100 = getAntialiasedColors(
                VALUE_TEXT_ANTIALIAS_LCD_HRGB, 100);

        if (colorsAAOnLCD100.size() <= 2) {
            throw new RuntimeException("Wrong number of antialiased colors.");
        }

        HashSet colorsAAOnLCD250 = getAntialiasedColors(
                VALUE_TEXT_ANTIALIAS_LCD_HRGB, 250);

        if (colorsAAOnLCD250.size() <= 2) {
            throw new RuntimeException("Wrong number of antialiased colors.");
        }

        if (colorsAAOnLCD100.equals(colorsAAOnLCD250)) {
            throw new RuntimeException("LCD contarst is not used.");
        }
    }

    private static HashSet getAntialiasedColors(Object aaHint, int lcdContrast) {

        JLabel label = new JLabel("ABCD");
        label.setSize(label.getPreferredSize());
        label.putClientProperty(KEY_TEXT_ANTIALIASING, aaHint);
        label.putClientProperty(KEY_TEXT_LCD_CONTRAST, lcdContrast);

        int w = label.getWidth();
        int h = label.getHeight();

        BufferedImage buffImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        label.paint(g);
        g.dispose();

        HashSet<Color> colors = new HashSet<>();

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Color color = new Color(buffImage.getRGB(i, j));
                colors.add(color);
            }
        }

        return colors;
    }

    private static void setMetalLookAndFeel() {
        setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    }

    private static void setLookAndFeel(String lafClass) {
        try {
            UIManager.setLookAndFeel(lafClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testLAFAAHints() {

        for (LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
            testLAFAAHints(lafInfo);
        }
    }

    private static final String[] EXCLUDED_LAFS = {"CDE/Motif"};

    private static boolean isExcludedLAF(LookAndFeelInfo lafInfo) {
        for (String excludedLaf : EXCLUDED_LAFS) {
            if (lafInfo.getName().equals(excludedLaf)) {
                return true;
            }
        }
        return false;
    }

    private static void testLAFAAHints(LookAndFeelInfo lafInfo) {
        setLookAndFeel(lafInfo.getClassName());

        Object uiAAHint = UIManager.getDefaults().get(KEY_TEXT_ANTIALIASING);
        Object uiLCDContrastHint = UIManager.getDefaults().get(
                KEY_TEXT_LCD_CONTRAST);

        Object aaHints = Toolkit.getDefaultToolkit().
                getDesktopProperty("awt.font.desktophints");

        if (isExcludedLAF(lafInfo)) {
            if (uiAAHint != null || uiLCDContrastHint != null) {
                throw new RuntimeException("Rendering hints set for excluded L&F");
            }
        } else if (aaHints instanceof Map) {
            Map map = (Map) aaHints;

            if (uiAAHint != map.get(KEY_TEXT_ANTIALIASING)) {
                throw new RuntimeException("UI defaults contains wrong aa hint");
            }

            if (uiLCDContrastHint != map.get(KEY_TEXT_LCD_CONTRAST)) {
                throw new RuntimeException("UI defaults contains wrong"
                        + "lcd contrast hint");
            }
        } else if (uiAAHint != null || uiLCDContrastHint != null) {
            throw new RuntimeException("Rendering hints set for empty desktop"
                    + "properties");
        }
    }

    private static class CustomLookAndFeel extends BasicLookAndFeel {

        private final boolean useAAHints;

        public CustomLookAndFeel(boolean useAAHints) {
            this.useAAHints = useAAHints;
        }

        @Override
        public String getDescription() {
            return getName();
        }

        @Override
        public String getName() {
            return "Custom L&F";
        }

        @Override
        public String getID() {
            return getName();
        }

        @Override
        public boolean isNativeLookAndFeel() {
            return false;
        }

        @Override
        public boolean isSupportedLookAndFeel() {
            return true;
        }

        @Override
        protected void initClassDefaults(UIDefaults table) {
            super.initClassDefaults(table);
            table.put(KEY_TEXT_ANTIALIASING, getAAHint());
            table.put(KEY_TEXT_LCD_CONTRAST, getLCDContarstHint());
        }

        private Object getAAHint() {
            return useAAHints ? VALUE_TEXT_ANTIALIAS_GASP : null;
        }

        private Object getLCDContarstHint() {
            return useAAHints ? 115 : null;
        }
    }
}
