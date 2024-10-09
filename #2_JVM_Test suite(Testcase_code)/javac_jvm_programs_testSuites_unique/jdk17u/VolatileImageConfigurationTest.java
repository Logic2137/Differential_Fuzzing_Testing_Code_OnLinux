import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class VolatileImageConfigurationTest extends JFrame implements ActionListener {

    private static JFrame testFrame;

    private static volatile boolean testComplete = false;

    private static volatile boolean testResult = false;

    private static final int TEST_WIDTH = 600;

    private static final int TEST_HEIGHT = 600;

    private static final int TEST_MIN_DURATION = 3000;

    private static final int TEST_TOTAL_DURATION = 45000;

    private JTextArea infoTextArea;

    private JPanel buttonPanel;

    private JPanel testPanel;

    private JButton passButton;

    private JButton failButton;

    public VolatileImageConfigurationTest() {
        super("Volatile Image Configuration Update Test");
        initComponents();
    }

    private void initComponents() {
        String description = "\n Volatile Image Configuration Update Test.\n" + " 1. The test displays scale values of component and the" + " underlying graphics device configuration.\n" + " 2. Kindly change the display's DPI settings from OS" + " control panel and observe the application.\n" + " 3. Select Pass if the scale values for both component & " + "underlying device configuration are updated as per the " + "\ndisplay's DPI value.\n";
        infoTextArea = new JTextArea(description);
        testPanel = new DisplayPanel();
        passButton = new JButton("Pass");
        passButton.setActionCommand("Pass");
        passButton.setEnabled(true);
        passButton.addActionListener(this);
        failButton = new JButton("Fail");
        failButton.setActionCommand("Fail");
        failButton.setEnabled(true);
        failButton.addActionListener(this);
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(passButton);
        buttonPanel.add(failButton);
        setLayout(new BorderLayout(10, 10));
        add(infoTextArea, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(testPanel, BorderLayout.CENTER);
        setSize(TEST_WIDTH, TEST_HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Pass")) {
            testComplete = true;
            testResult = true;
            System.out.println("Test Passed.");
        } else if (command.equals("Fail")) {
            testComplete = true;
            testResult = false;
        }
    }

    private static void constructTestUI() {
        testFrame = new VolatileImageConfigurationTest();
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setLocationRelativeTo(null);
        testFrame.setVisible(true);
    }

    private static void destructTestUI() {
        testFrame.dispose();
    }

    static class DisplayPanel extends JPanel {

        private static final int PANEL_WIDTH = 600;

        private static final int PANEL_HEIGHT = 500;

        private static final int PANEL_X = 20;

        private static final int PANEL_Y = 80;

        private static final String MSG = "%s scale: [%2.2f, %2.2f]";

        private VolatileImage vImg;

        public DisplayPanel() throws HeadlessException {
            setSize(PANEL_WIDTH, PANEL_HEIGHT);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
            drawInfo(g, PANEL_X, PANEL_Y, "Frame", Color.BLUE);
            int attempts = 0;
            do {
                drawBackingStoreImage(g);
            } while (vImg.contentsLost() && ++attempts < 3);
        }

        private void drawInfo(Graphics g, int x, int y, String msg, Color color) {
            g.setColor(color);
            g.setFont(g.getFont().deriveFont(24f));
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform tx = g2d.getTransform();
            g.drawString(msg, x, y);
            String text = String.format(MSG, "Graphics", tx.getScaleX(), tx.getScaleY());
            int dy = 20;
            g.drawString(text, x, y + dy);
            tx = g2d.getDeviceConfiguration().getDefaultTransform();
            text = String.format(MSG, "Device Config", tx.getScaleX(), tx.getScaleY());
            g.drawString(text, x, y + 2 * dy);
        }

        private void drawBackingStoreImage(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            GraphicsConfiguration gc = g2d.getDeviceConfiguration();
            if (vImg == null || vImg.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
                vImg = createVolatileImage(PANEL_WIDTH, PANEL_HEIGHT / 3);
            }
            Graphics vImgGraphics = vImg.createGraphics();
            vImgGraphics.setColor(Color.WHITE);
            vImgGraphics.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT / 3);
            drawInfo(vImgGraphics, PANEL_X, PANEL_Y, "Backbuffer", Color.MAGENTA);
            g.drawImage(vImg, 0, PANEL_Y * 2, this);
        }
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                try {
                    constructTestUI();
                } catch (Exception ex) {
                    throw new RuntimeException("Test Failed. Error while " + "creating the test interface.");
                }
            }
        });
        try {
            long totalWaitDuration = 0;
            do {
                Thread.sleep(TEST_MIN_DURATION);
                totalWaitDuration += TEST_MIN_DURATION;
            } while (!testComplete && totalWaitDuration < TEST_TOTAL_DURATION);
        } catch (InterruptedException ite) {
        }
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                try {
                    destructTestUI();
                } catch (Exception ex) {
                }
            }
        });
        if (testResult == false) {
            throw new RuntimeException("Test Failed. Incorrect scale values " + "were seen during the test execution.");
        }
    }
}
