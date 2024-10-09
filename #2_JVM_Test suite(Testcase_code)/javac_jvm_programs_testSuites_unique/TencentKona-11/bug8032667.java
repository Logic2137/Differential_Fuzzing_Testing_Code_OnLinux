
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;


public class bug8032667 extends JApplet {

    static final int scale = 2;
    static final int width = 130;
    static final int height = 50;
    static final int scaledWidth = scale * width;
    static final int scaledHeight = scale * height;

    @Override
    public void init() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                final Image image1 = getImage(getCheckBox("Deselected", false));
                final Image image2 = getImage(getCheckBox("Selected", true));

                Canvas canvas = new Canvas() {

                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);
                        g.drawImage(image1, 0, 0, scaledWidth, scaledHeight, this);
                        g.drawImage(image2, 0, scaledHeight + 5,
                                scaledWidth, scaledHeight, this);
                    }
                };

                getContentPane().add(canvas, BorderLayout.CENTER);
            }
        });
    }

    static JCheckBox getCheckBox(String text, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setSelected(selected);
        checkBox.setSize(new Dimension(width, height));
        return checkBox;
    }

    static Image getImage(JComponent component) {
        final BufferedImage image = new BufferedImage(
                scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        final Graphics g = image.getGraphics();
        ((Graphics2D) g).scale(scale, scale);
        component.paint(g);
        g.dispose();

        return image;
    }
}
