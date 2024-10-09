



import javax.swing.*;
import java.awt.*;

public class bug6989617 {
    private static MyPanel panel;
    private static JButton button;

    public static void main(String... args) throws Exception {
        Robot robot = new Robot();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame. setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                panel = new MyPanel();

                button = new JButton("Hello");
                panel.add(button);
                frame.add(panel);

                frame.setSize(200, 300);
                frame.setVisible(true);
            }
        });
        
        
        
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                panel.resetPaintRectangle();
                button.repaint();
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                Rectangle pr = panel.getPaintRectangle();
                if (!pr.getSize().equals(button.getSize())) {
                    throw new RuntimeException("wrong size of the dirty area");
                }
                if (!pr.getLocation().equals(button.getLocation())) {
                    throw new RuntimeException("wrong location of the dirty area");
                }
            }
        });
        
        
        
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                panel.resetPaintRectangle();
                panel.setPaintingOrigin(false);
                if (panel.getPaintRectangle() != null) {
                    throw new RuntimeException("paint rectangle is not null");
                }
                button.repaint();
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                if(panel.getPaintRectangle() != null) {
                    throw new RuntimeException("paint rectangle is not null");
                }
                System.out.println("Test passed...");
            }
        });
    }

    static class MyPanel extends JPanel {
        private boolean isPaintingOrigin = true;
        private Rectangle paintRectangle;

        {
            setLayout(new GridBagLayout());
        }

        public boolean isPaintingOrigin() {
            return isPaintingOrigin;
        }

        public void setPaintingOrigin(boolean paintingOrigin) {
            isPaintingOrigin = paintingOrigin;
        }

        public void paintImmediately(int x, int y, int w, int h) {
            super.paintImmediately(x, y, w, h);
            paintRectangle = new Rectangle(x, y, w, h);
        }

        public Rectangle getPaintRectangle() {
            return paintRectangle == null? null: new Rectangle(paintRectangle);
        }

        public void resetPaintRectangle() {
            this.paintRectangle = null;
        }
    }
}
