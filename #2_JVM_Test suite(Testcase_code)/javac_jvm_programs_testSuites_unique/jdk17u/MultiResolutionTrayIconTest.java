import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MultiResolutionTrayIconTest {

    private static SystemTray tray;

    private static TrayIcon icon;

    private static GridBagLayout layout;

    private static JPanel mainControlPanel;

    private static JPanel resultButtonPanel;

    private static JLabel instructionText;

    private static JButton passButton;

    private static JButton failButton;

    private static JButton startButton;

    private static JFrame mainFrame;

    private static CountDownLatch latch;

    public static void main(String[] args) throws Exception {
        latch = new CountDownLatch(1);
        createUI();
        latch.await(200, TimeUnit.SECONDS);
    }

    public static void createUI() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                mainFrame = new JFrame("TrayIcon Test");
                if (!SystemTray.isSupported()) {
                    System.out.println("system tray is not supported");
                    latch.countDown();
                    return;
                }
                tray = SystemTray.getSystemTray();
                Dimension d = tray.getTrayIconSize();
                icon = new TrayIcon(createIcon(d.width, d.height));
                icon.setImageAutoSize(true);
                layout = new GridBagLayout();
                mainControlPanel = new JPanel(layout);
                resultButtonPanel = new JPanel(layout);
                GridBagConstraints gbc = new GridBagConstraints();
                String instructions = "<html>INSTRUCTIONS:<br>" + "Press start button to add icon to system tray.<br><br>" + "If Icon color is green test" + " passes else failed.<br><br></html>";
                instructionText = new JLabel();
                instructionText.setText(instructions);
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                mainControlPanel.add(instructionText, gbc);
                startButton = new JButton("Start");
                startButton.setActionCommand("Start");
                startButton.addActionListener((ActionEvent e) -> {
                    doTest();
                });
                gbc.gridx = 0;
                gbc.gridy = 0;
                resultButtonPanel.add(startButton, gbc);
                passButton = new JButton("Pass");
                passButton.setActionCommand("Pass");
                passButton.addActionListener((ActionEvent e) -> {
                    latch.countDown();
                    removeIcon();
                    mainFrame.dispose();
                });
                failButton = new JButton("Fail");
                failButton.setActionCommand("Fail");
                failButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeIcon();
                        latch.countDown();
                        mainFrame.dispose();
                        throw new RuntimeException("Test Failed");
                    }
                });
                gbc.gridx = 1;
                gbc.gridy = 0;
                resultButtonPanel.add(passButton, gbc);
                gbc.gridx = 2;
                gbc.gridy = 0;
                resultButtonPanel.add(failButton, gbc);
                gbc.gridx = 0;
                gbc.gridy = 1;
                mainControlPanel.add(resultButtonPanel, gbc);
                mainFrame.add(mainControlPanel);
                mainFrame.setSize(400, 200);
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);
                mainFrame.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        removeIcon();
                        latch.countDown();
                        mainFrame.dispose();
                    }
                });
            }
        });
    }

    private static BaseMultiResolutionImage createIcon(int w, int h) {
        return new BaseMultiResolutionImage(new BufferedImage[] { generateImage(w, h, 1, Color.RED), generateImage(w, h, 2, Color.GREEN) });
    }

    private static BufferedImage generateImage(int w, int h, int scale, Color c) {
        int x = w * scale, y = h * scale;
        BufferedImage img = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setColor(c);
        g.fillRect(0, 0, x, y);
        g.setColor(Color.WHITE);
        g.fillRect(x / 3, y / 3, x / 3, y / 3);
        return img;
    }

    private static void doTest() {
        if (tray.getTrayIcons().length > 0) {
            return;
        }
        try {
            tray.add(icon);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void removeIcon() {
        if (tray != null) {
            tray.remove(icon);
        }
    }
}
