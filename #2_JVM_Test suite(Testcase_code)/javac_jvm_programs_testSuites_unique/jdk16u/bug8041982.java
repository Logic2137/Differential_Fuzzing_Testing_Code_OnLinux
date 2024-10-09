



import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class bug8041982 extends JFrame {

    public bug8041982() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JLayer<>(new JPanel(), new BusyLayer()));
        setSize(200, 300);
        setVisible(true);
    }

    public static void main(String... args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new bug8041982().setVisible(true);
            }
        });
        Thread.sleep(5000);
    }

    private class BusyLayer extends LayerUI<JComponent> {
        private volatile boolean animated = true;
        private Icon icon = new ImageIcon(bug8041982.class.getResource("duke.gif"));
        private int imageUpdateCount;

        @Override
        public void paint(Graphics g, JComponent c) {
            super.paint(g, c);
            if (isAnimated()) {
                icon.paintIcon(c, g, c.getWidth() / 2 - icon.getIconWidth() /
                        2,
                        c.getHeight() / 2 - icon.getIconHeight() / 2);
            }
        }

        public boolean isAnimated() {
            return animated;
        }

        public void setAnimated(boolean animated) {
            if (this.animated != animated) {
                this.animated = animated;
                firePropertyChange("animated", !animated, animated);
            }
        }

        @Override
        public void applyPropertyChange(PropertyChangeEvent evt, JLayer l) {
            
            l.repaint();
        }

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h, JLayer<? extends JComponent> l) {
            System.out.println("imageUpdate " + imageUpdateCount);
            if (imageUpdateCount++ == 100) {
                setAnimated(false);
            } else if (imageUpdateCount > 100) {
                throw new RuntimeException("Test failed");
            }
            return isAnimated() && super.imageUpdate(img, infoflags, x, y, w, h, l);
        }
    }
}

