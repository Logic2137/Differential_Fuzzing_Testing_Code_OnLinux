



import javax.swing.*;
import java.awt.image.BufferedImage;

public class bug7141573 {
    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            System.out.println("WindowsLookAndFeel is not supported. The test bug7141573 is skipped.");

            return;
        }

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

                JProgressBar bar = new JProgressBar();

                bar.setStringPainted(true);

                bar.setSize(100, 1);
                bar.paint(image.getGraphics());

                bar.setSize(1, 100);
                bar.paint(image.getGraphics());

                System.out.println("The test bug7141573 is passed.");
            }
        });
    }
}
