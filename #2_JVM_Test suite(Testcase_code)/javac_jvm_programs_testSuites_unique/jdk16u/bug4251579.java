



import java.awt.*;
import javax.swing.*;

public class bug4251579 {

    private static JLabel htmlComponent;

    public static void main(String[] args) throws Exception {
        final Robot robot = new Robot();
        robot.setAutoDelay(50);

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });

        robot.waitForIdle();

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
    }

    private static void createAndShowGUI() {

        String htmlText =
                "<html>"
                + "<head><style> .blue{ color:blue; } </style></head>"
                + "<body"
                + "<P class=\"blue\"> should be rendered with BLUE class definition</P>"
                + "</body>";

        JFrame mainFrame = new JFrame("bug4251579");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        htmlComponent = new JLabel(htmlText);
        mainFrame.getContentPane().add(htmlComponent);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
