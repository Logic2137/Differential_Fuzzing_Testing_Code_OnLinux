




import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;

public class bug4251579 {

    private static JLabel htmlComponent;
    private static JFrame mainFrame;

    public static void main(String[] args) throws Exception {
        final Robot robot = new Robot();

        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    createAndShowGUI();
                }
            });

            robot.waitForIdle();
            robot.delay(1000);

            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    boolean passed = false;

                    Point p = htmlComponent.getLocationOnScreen();
                    Dimension d = htmlComponent.getSize();
                    int x0 = p.x;
                    int y = p.y + d.height / 2;

                    for (int x = x0; x < x0 + d.width; x++) {
                        if (robot.getPixelColor(x, y).equals(Color.blue)) {
                            passed = true;
                            break;
                        }
                    }

                    if (!passed) {
                        throw new RuntimeException("Test failed.");
                    }

                }
            });
        } finally {
            SwingUtilities.invokeAndWait(() -> {
                if (mainFrame != null) {
                    mainFrame.dispose();
                }
            });
        }
    }

    private static void createAndShowGUI() {

        String htmlText =
                "<html>"
                + "<head><style> .blue{ color:blue; } </style></head>"
                + "<body"
                + "<P class=\"blue\"> should be rendered with BLUE class definition</P>"
                + "</body>";

        mainFrame = new JFrame("bug4251579");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        htmlComponent = new JLabel(htmlText);
        mainFrame.getContentPane().add(htmlComponent);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
