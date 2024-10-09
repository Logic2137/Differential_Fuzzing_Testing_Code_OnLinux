

 
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class bug8057791 {

    private static JFrame frame;
    private static JList<String> list;
    private static DefaultListModel<String> model;
    private static Robot robot;
    private static final int SELECTED_INDEX = 0;
    private static volatile String errorString = "";

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.waitForIdle();
        runSteps();
        if (!errorString.isEmpty()) {
            throw new RuntimeException("Error Log:\n" + errorString);
        }
    }

    private static void runSteps() throws Exception {
        if (tryNimbusLookAndFeel()) {
            createUI();
            robot.waitForIdle();
            runColorTestCase();
            robot.waitForIdle();
            cleanUp();
        }

    }

    private static boolean tryNimbusLookAndFeel()
            throws Exception {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            errorString += e.getMessage();
            return false;
        }
        return true;
    }

    private static void createUI()
            throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                list = new JList<>();
                model = new DefaultListModel<>();
                model.add(0, "@@");
                Font font = list.getFont();
                list.setFont(
                        new Font(font.getFontName(),
                                Font.BOLD,
                                (int) (font.getSize2D() * 2)));
                list.setModel(model);
                list.setSelectedIndex(SELECTED_INDEX);
                frame.add(list);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    private static void runColorTestCase() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                Rectangle cellBounds = list.getCellBounds(SELECTED_INDEX, SELECTED_INDEX);
                cellBounds.x += list.getLocationOnScreen().x;
                cellBounds.y += list.getLocationOnScreen().y;
                
                Color foregroundColor = list.getSelectionForeground();
                Color backgroundColor = list.getSelectionBackground();
                checkColor(foregroundColor, cellBounds);
                checkColor(backgroundColor, cellBounds);
                
                Color changedForegroundColor = foregroundColor.darker();
                Color changedBackgroundColor = backgroundColor.brighter();
                checkNotColor(changedForegroundColor, cellBounds);
                checkNotColor(changedBackgroundColor, cellBounds);
            }
        });
    }

    private static void checkColor(Color colorCheck, Rectangle bounds) {
        if (!findColor(colorCheck, bounds)) {
            String error = "[ERROR][" + colorCheck.toString()
                    + "] Not found in selected cell";
            errorString += error;
        }
    }

    private static void checkNotColor(Color colorCheck, Rectangle bounds) {
        if (findColor(colorCheck, bounds)) {
            String error = "[ERROR][" + colorCheck.toString()
                    + "] is found in selected cell. "
                    + "Not supposed to be found in negative test case";
            errorString += error;
        }
    }

    private static boolean findColor(Color colorCheck, Rectangle bounds) {
        BufferedImage img = new BufferedImage(bounds.width,
                bounds.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        list.paint(g);
        g.dispose();
        checkLoops:
        for (int x = 0; x < bounds.width; x++) {
            for (int y = 0; y < bounds.height; y++) {
                Point relativePointCheck = new Point(bounds.x + x, bounds.y + y);
                robot.mouseMove(relativePointCheck.x, relativePointCheck.y);
                Color detectedColor = robot.getPixelColor(relativePointCheck.x,
                        relativePointCheck.y);
                if (detectedColor.equals(colorCheck)) {
                    return true;
                }
                if (isMac()) {
                    
                    detectedColor = new Color(img.getRGB(x, y), true);
                    if (detectedColor.equals(colorCheck)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isMac() {
        String osName = System.getProperty("os.name");
        return osName.contains("Mac");
    }

    private static void cleanUp() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                frame.dispose();
            }
        });
    }
}
