import java.awt.AWTException;
import java.awt.Button;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.TextArea;
import java.awt.TrayIcon;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.MenuItem;
import java.awt.SystemTray;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AddPopupAfterShowTest {

    private static final Frame instructionFrame = new Frame();

    private static TrayIcon trayIcon = null;

    private static volatile boolean testResult = false;

    private static volatile CountDownLatch countDownLatch;

    private static final String INSTRUCTIONS = "INSTRUCTIONS:\n\n" + "1) The red circle icon was added to the system tray.\n" + "2) Check that a popup menu is opened when the icon is clicked.\n" + "3) If true the test is passed, otherwise failed.";

    public static void main(String[] args) throws Exception {
        if (!SystemTray.isSupported()) {
            System.out.println("The System Tray is not supported," + "so this test can not be run in this scenario.");
            return;
        }
        countDownLatch = new CountDownLatch(1);
        createInstructionUI();
        createTestUI();
        countDownLatch.await(15, TimeUnit.MINUTES);
        disposeUI();
        if (!testResult) {
            throw new RuntimeException("Test failed!");
        }
    }

    private static void createInstructionUI() {
        GridBagLayout layout = new GridBagLayout();
        Panel mainControlPanel = new Panel(layout);
        Panel resultButtonPanel = new Panel(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        TextArea instructionTextArea = new TextArea();
        instructionTextArea.setText(INSTRUCTIONS);
        instructionTextArea.setEditable(false);
        instructionTextArea.setBackground(Color.white);
        mainControlPanel.add(instructionTextArea, gbc);
        Button passButton = new Button("Pass");
        passButton.setActionCommand("Pass");
        passButton.addActionListener((ActionEvent e) -> {
            testResult = true;
            countDownLatch.countDown();
        });
        Button failButton = new Button("Fail");
        failButton.setActionCommand("Fail");
        failButton.addActionListener(e -> {
            countDownLatch.countDown();
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        resultButtonPanel.add(passButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        resultButtonPanel.add(failButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainControlPanel.add(resultButtonPanel, gbc);
        instructionFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                countDownLatch.countDown();
            }
        });
        instructionFrame.pack();
        instructionFrame.add(mainControlPanel);
        instructionFrame.pack();
        instructionFrame.setVisible(true);
    }

    private static void createTestUI() throws Exception {
        trayIcon = new TrayIcon(createTrayIconImage());
        trayIcon.setImageAutoSize(true);
        SystemTray.getSystemTray().add(trayIcon);
        trayIcon.setPopupMenu(createTrayIconPopupMenu());
    }

    private static Image createTrayIconImage() {
        int trayIconImageSize = 32;
        final BufferedImage trayImage = new BufferedImage(trayIconImageSize, trayIconImageSize, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D trayImageGraphics = (Graphics2D) trayImage.getGraphics();
        trayImageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        trayImageGraphics.setColor(new Color(255, 255, 255, 0));
        trayImageGraphics.fillRect(0, 0, trayImage.getWidth(), trayImage.getHeight());
        trayImageGraphics.setColor(Color.red);
        int trayIconImageInset = 4;
        trayImageGraphics.fillOval(trayIconImageInset, trayIconImageInset, trayImage.getWidth() - 2 * trayIconImageInset, trayImage.getHeight() - 2 * trayIconImageInset);
        trayImageGraphics.setColor(Color.darkGray);
        trayImageGraphics.drawOval(trayIconImageInset, trayIconImageInset, trayImage.getWidth() - 2 * trayIconImageInset, trayImage.getHeight() - 2 * trayIconImageInset);
        return trayImage;
    }

    private static PopupMenu createTrayIconPopupMenu() {
        final PopupMenu trayIconPopupMenu = new PopupMenu();
        final MenuItem popupMenuItem = new MenuItem("TEST PASSED!");
        trayIconPopupMenu.add(popupMenuItem);
        return trayIconPopupMenu;
    }

    private static void disposeUI() {
        SystemTray.getSystemTray().remove(trayIcon);
        instructionFrame.dispose();
    }
}
