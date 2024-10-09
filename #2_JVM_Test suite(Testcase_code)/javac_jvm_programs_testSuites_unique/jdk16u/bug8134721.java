

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;


public class bug8134721 {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(bug8134721::testNPE);
    }

    private static void testNPE() {

        Graphics g = null;
        try {
            String test = "\ttest\ttest2";
            BufferedImage buffImage = new BufferedImage(
                    100, 100, BufferedImage.TYPE_INT_RGB);
            g = buffImage.createGraphics();
            Segment segment = new Segment(test.toCharArray(), 0, test.length());
            Utilities.drawTabbedText(segment, 0, 0, g, null, 0);
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
    }
}
