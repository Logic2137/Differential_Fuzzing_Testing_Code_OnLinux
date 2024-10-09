

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;


public class bug8041561 {

    private static JRadioButton radioButton;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                try {
                    MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                    UIManager.setLookAndFeel(new MetalLookAndFeel());
                    createAndShowGUI();
                } catch (UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        new Robot().waitForIdle();
        Thread.sleep(500);

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                try {
                    Point point = radioButton.getLocationOnScreen();
                    int x = (int) point.getX() + radioButton.getWidth() / 2;
                    int y = (int) point.getY() + radioButton.getHeight() / 2;

                    Robot robot = new Robot();
                    Color color = robot.getPixelColor(x, y);
                    if (!Color.BLUE.equals(color)) {
                        throw new RuntimeException("JRadioButton is opaque");
                    }
                } catch (AWTException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLUE);
        radioButton = new JRadioButton();
        radioButton.setOpaque(false);
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLUE);
        panel.add(radioButton);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
