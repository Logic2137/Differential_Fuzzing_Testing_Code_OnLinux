import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalLabelUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class TitledBorderLabelUITest {

    private static final int SIZE = 50;

    private static boolean useLAF;

    public static void main(String[] args) throws Exception {
        useLAF = "LAF".equals(args[0]);
        SwingUtilities.invokeAndWait(TitledBorderLabelUITest::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(new TestLookAndFeel());
            JLabel label = new JLabel("Test Label");
            label.setSize(SIZE, SIZE);
            TitledBorder border = new TitledBorder("ABCDEF");
            label.setBorder(new TitledBorder(border));
            if (useLAF) {
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
            } else {
                UIManager.getDefaults().put("LabelUI", MetalLabelUI.class.getName());
            }
            SwingUtilities.updateComponentTreeUI(label);
            paintToImage(label);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void paintToImage(JComponent comp) {
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        comp.paint(g);
        g.dispose();
    }

    public static class TestLookAndFeel extends MetalLookAndFeel {

        @Override
        protected void initClassDefaults(UIDefaults table) {
            super.initClassDefaults(table);
            table.put("LabelUI", TestLabelUI.class.getName());
        }
    }

    public static class TestLabelUI extends MetalLabelUI implements UIResource {

        public static ComponentUI createUI(JComponent c) {
            return new TestLabelUI();
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            super.paint(g, c);
            throw new RuntimeException("New LabelUI is not installed!");
        }
    }
}
