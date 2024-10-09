

import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;


public final class AnimatedIcon {

    public static void main(final String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            final BufferedImage bi = new BufferedImage(1, 1, TYPE_INT_RGB);
            final ImageIcon icon = new ImageIcon(bi);
            final JButton button = new JButton(icon);
            
            isAnimated(bi, button);
            button.getModel().setPressed(true);
            button.getModel().setArmed(true);
            isAnimated(bi, button);
            button.getModel().setPressed(false);
            button.getModel().setArmed(false);
            button.getModel().setSelected(true);
            isAnimated(bi, button);
            button.getModel().setSelected(false);
            button.getModel().setRollover(true);
            button.setRolloverEnabled(true);
            isAnimated(bi, button);
            button.getModel().setSelected(true);
            isAnimated(bi, button);
            
            
            button.setIcon(null);
            button.setPressedIcon(icon);
            button.getModel().setPressed(true);
            button.getModel().setArmed(true);
            isAnimated(bi, button);
        });
    }

    private static void isAnimated(BufferedImage bi, JButton button) {
        if (!button.imageUpdate(bi, ImageObserver.SOMEBITS, 0, 0, 1, 1)) {
            throw new RuntimeException();
        }
    }
}
