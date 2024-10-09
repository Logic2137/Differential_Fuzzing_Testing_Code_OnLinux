import javax.swing.*;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthSliderUI;
import java.awt.*;
import java.awt.image.BufferedImage;

public class bug6923305 {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new SynthLookAndFeel());
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                JSlider slider = new JSlider();
                slider.setUI(new SynthSliderUI(slider) {

                    @Override
                    protected void paintTrack(SynthContext context, Graphics g, Rectangle trackBounds) {
                        throw new RuntimeException("Test failed: the SynthSliderUI.paintTrack was invoked");
                    }
                });
                slider.setPaintTrack(false);
                slider.setSize(slider.getPreferredSize());
                BufferedImage bufferedImage = new BufferedImage(slider.getWidth(), slider.getHeight(), BufferedImage.TYPE_INT_ARGB);
                slider.paint(bufferedImage.getGraphics());
            }
        });
    }
}
