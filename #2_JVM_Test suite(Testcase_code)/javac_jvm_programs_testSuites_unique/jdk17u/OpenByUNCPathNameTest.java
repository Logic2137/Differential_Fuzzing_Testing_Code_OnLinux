import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.io.IOException;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class OpenByUNCPathNameTest {

    private static volatile CountDownLatch countDownLatch;

    private static JFrame instructionFrame;

    private static JFrame testFrame;

    private static volatile boolean testPassed = false;

    private static File file;

    private static boolean validatePlatform() {
        String osName = System.getProperty("os.name");
        if (osName == null) {
            throw new RuntimeException("Name of the current OS could not be" + " retrieved.");
        }
        return osName.startsWith("Windows");
    }

    private static void createInstructionUI() {
        SwingUtilities.invokeLater(() -> {
            String instructions = "1. Make sure that disk C is shared \n" + "2. Click on openFileByLocalPath Button to test Test" + " opening of the file with Windows local file path\n" + "3. Check that the file is opened successfully\n" + "4. Close the file\n" + "5. Click on openFileByUNCPath Button to test Test" + " opening of the file with Windows UNC pathname\n" + "6. Check that the file is opened successfully\n" + "7. Close the file\n" + "8. If all the conditions are met then press PASS else " + "press FAIL";
            instructionFrame = new JFrame("InstructionFrame");
            JTextArea textArea = new JTextArea(instructions);
            textArea.setEditable(false);
            final JButton passButton = new JButton("PASS");
            passButton.addActionListener((e) -> {
                testPassed = true;
                instructionFrame.dispose();
                testFrame.dispose();
                file.delete();
                countDownLatch.countDown();
            });
            final JButton failButton = new JButton("FAIL");
            failButton.addActionListener((e) -> {
                instructionFrame.dispose();
                testFrame.dispose();
                file.delete();
                countDownLatch.countDown();
            });
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(textArea, BorderLayout.CENTER);
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(passButton);
            buttonPanel.add(failButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            instructionFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            instructionFrame.setBounds(0, 0, 500, 500);
            instructionFrame.add(mainPanel);
            instructionFrame.pack();
            instructionFrame.setVisible(true);
        });
    }

    private static void openFile() throws IOException {
        if (!Desktop.isDesktopSupported()) {
            System.out.println("java.awt.Desktop is not supported on this" + " platform.");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.OPEN)) {
            System.out.println("Action.OPEN is not supported on this" + " platform.");
            return;
        }
        file = File.createTempFile("Read Me File", ".txt");
        testFrame = new JFrame("Test Frame");
        JPanel testButtonPanel = new JPanel(new FlowLayout());
        final JButton openFileByLocalPathButton = new JButton("OpenFileByLocalPath");
        final JButton openFileByUNCPathButton = new JButton("OpenFileByUNCPath");
        openFileByLocalPathButton.addActionListener((e) -> {
            try {
                desktop.open(file);
            } catch (IOException ioException) {
            }
        });
        SwingUtilities.invokeLater(() -> {
            testButtonPanel.add(openFileByLocalPathButton);
            testButtonPanel.add(openFileByUNCPathButton);
            testFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            testFrame.setBounds(600, 0, 1000, 200);
            testFrame.add(testButtonPanel);
            testFrame.pack();
            testFrame.setVisible(true);
        });
        String uncFilePath = "\\\\127.0.0.1\\" + file.getAbsolutePath().replace(':', '$');
        File uncFile = new File(uncFilePath);
        if (!uncFile.exists()) {
            throw new RuntimeException(String.format("File " + "with UNC pathname '%s' does not exist.", uncFilePath));
        }
        openFileByUNCPathButton.addActionListener((e) -> {
            try {
                desktop.open(uncFile);
            } catch (IOException ioException) {
            }
        });
    }

    public static void main(String[] args) throws Exception {
        if (!validatePlatform()) {
            System.out.println("This test is only for MS Windows OS.");
            return;
        }
        countDownLatch = new CountDownLatch(1);
        OpenByUNCPathNameTest openByUNCPathNameTest = new OpenByUNCPathNameTest();
        openByUNCPathNameTest.createInstructionUI();
        openByUNCPathNameTest.openFile();
        countDownLatch.await(15, TimeUnit.MINUTES);
        if (!testPassed) {
            throw new RuntimeException("Test failed!");
        }
    }
}
