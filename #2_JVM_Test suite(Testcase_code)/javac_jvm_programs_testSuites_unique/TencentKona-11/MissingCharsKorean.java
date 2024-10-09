




import java.awt.AWTException;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Robot;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.event.KeyEvent;
import java.util.concurrent.CountDownLatch;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MissingCharsKorean {
    private static boolean testPassed = false;
    private static boolean startTest = false;
    private static int expectedResults[] = null;
    private static int inKeyCodes[][] = null;

    private static JFrame frame = null;
    private static JLabel lblTestStatus = null;
    private static JTextField textFieldMain = null;
    private static String testResult;

    private static final CountDownLatch testStartLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            setupUI();
        });

        testStartLatch.await();

        if (startTest) {
            glyphTest();

            frame.dispose();

            if (testPassed) {
                System.out.println(testResult);
            } else {
                throw new RuntimeException("Korean text missing characters : "
                        + testResult);
            }
        } else {
            throw new RuntimeException("User has not executed the test");
        }
    }

    private static void setupUI() {
        String description = " 1. Go to \"System Preferences -> Keyboard -> "
                            + "Input Sources\" and add \"2-Set Korean\""
                            + " from Korean language group \n"
                            + " 2. Set current IM to \"2-Set Korean\" \n"
                            + " 3. Try typing in the text field to ensure"
                            + " that Korean keyboard has been successfully"
                            + " selected \n"
                            + " 4. Now click on \"Start Test\" button \n";
        String title = "Missing Characters Korean Test (Mac OS)";

        frame = new JFrame(title);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel textEditPanel = new JPanel(new FlowLayout());

        textFieldMain = new JTextField(20);
        Font font = new Font("Source Han Serif K", Font.BOLD,12);
        textFieldMain.setFont(font);

        textEditPanel.add(textFieldMain);

        mainPanel.add(textEditPanel, BorderLayout.CENTER);

        JTextArea textArea = new JTextArea(description);
        textArea.setEditable(false);
        final JButton btnStartTest = new JButton("Start Test");
        final JButton btnCancelTest = new JButton("Cancel Test");

        btnStartTest.addActionListener((e) -> {
            btnStartTest.setEnabled(false);
            btnCancelTest.setEnabled(false);
            startTest = true;
            testStartLatch.countDown();
        });

        btnCancelTest.addActionListener((e) -> {
            frame.dispose();
            testStartLatch.countDown();
        });
        mainPanel.add(textArea, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnStartTest);
        buttonPanel.add(btnCancelTest);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        lblTestStatus = new JLabel("");
        lblTestStatus.setMinimumSize(new Dimension(150, 20));
        lblTestStatus.setPreferredSize(new Dimension(150, 20));
        lblTestStatus.setVisible(true);
        textEditPanel.add(lblTestStatus);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                testStartLatch.countDown();
            }
            @Override
            public void windowOpened( WindowEvent e ){
                textFieldMain.requestFocusInWindow();
            }
        });

        frame.setVisible(true);
    }

    private static void glyphTest() {
        try {
            Robot robotKeySimulator = new Robot();
            performTasks(robotKeySimulator);
        } catch (AWTException e) {
            System.err.print("Creation Of Robot Failed : " + e.getMessage());
            testPassed = false;
        }
    }

    public static void performTasks(Robot robotForKeyInput) {
        int taskCount = 0;

        lblTestStatus.setText("Running Tests..");
        robotForKeyInput.setAutoDelay(500);

        while (setKeyInput(taskCount)) {
            textFieldMain.setText("");
            textFieldMain.requestFocusInWindow();
            enterInput(robotForKeyInput, inKeyCodes);
            taskCount++;

            try {
                SwingUtilities.invokeAndWait(() -> {
                    validateInput();
                });
            } catch (Exception e) {
                System.err.print("validateInput Failed : " + e.getMessage());
                testPassed = false;
                break;
            }

            if (!testPassed) {
                break;
            }
            setTaskStatus(false, taskCount);
        }
        setTaskStatus(true, taskCount);
    }

    private static boolean setKeyInput(int iCount) {
        boolean inputSet = true;

        switch(iCount) {
            case 0:
                
                expectedResults = new int[]{ 12610, 47 };
                inKeyCodes = new int[][] {  {KeyEvent.VK_Q},
                                            {KeyEvent.VK_SLASH}
                                        };
                break;

            case 1:
                
                expectedResults = new int[]{ 12610, 47, 54840, 92 };
                inKeyCodes = new int[][] {  {KeyEvent.VK_Q},
                                            {KeyEvent.VK_SLASH},
                                            {KeyEvent.VK_G},
                                            {KeyEvent.VK_H},
                                            {KeyEvent.VK_BACK_SLASH}
                                        };
                break;

            case 2:
                
                expectedResults = new int[]{ 12610, 47, 54857, 92 };
                inKeyCodes = new int[][] {  {KeyEvent.VK_Q},
                                            {KeyEvent.VK_SLASH},
                                            {KeyEvent.VK_G},
                                            {KeyEvent.VK_H},
                                            {KeyEvent.VK_Q},
                                            {KeyEvent.VK_BACK_SLASH}
                                        };
                break;

            case 3:
                
                expectedResults = new int[]{ 12610, 47, 54840, 92 };
                inKeyCodes = new int[][] {  {KeyEvent.VK_Q},
                                            {KeyEvent.VK_SLASH},
                                            {KeyEvent.VK_G},
                                            {KeyEvent.VK_H},
                                            {KeyEvent.VK_Q},
                                            {KeyEvent.VK_BACK_SPACE},
                                            {KeyEvent.VK_BACK_SLASH}
                                        };
                break;

            case 4:
                
                expectedResults = new int[]{ 12610, 47, 12622, 92 };
                inKeyCodes = new int[][] {  {KeyEvent.VK_Q},
                                            {KeyEvent.VK_SLASH},
                                            {KeyEvent.VK_G},
                                            {KeyEvent.VK_H},
                                            {KeyEvent.VK_Q},
                                            {KeyEvent.VK_BACK_SPACE},
                                            {KeyEvent.VK_BACK_SPACE},
                                            {KeyEvent.VK_BACK_SLASH}
                                        };
                break;

            default:
                inputSet = false;
                break;
        }

        return inputSet;
    }

    private static void enterInput(Robot robotKeyInput, int keyInputs[][]) {
        for (int i = 0; i < keyInputs.length; i++) {
            String strKeyInput = "KeyPress=>";
            final int noOfKeyInputs = keyInputs[i].length;
            for (int j = 0; j < noOfKeyInputs; j++) {
                robotKeyInput.keyPress(keyInputs[i][j]);
                strKeyInput += (Integer.toHexString(keyInputs[i][j])) + ":";
            }

            strKeyInput += "KeyRelease=>";
            for (int j = noOfKeyInputs - 1; j >= 0; j--) {
                robotKeyInput.keyRelease(keyInputs[i][j]);
                strKeyInput += (Integer.toHexString(keyInputs[i][j])) + ":";
            }
            System.out.println(strKeyInput);
        }
    }

    private static void validateInput() {
        testPassed = false;

        if (expectedResults != null) {
            String strCurr = textFieldMain.getText();
            if (expectedResults.length == strCurr.length()) {
                testPassed = true;

                for (int i = 0; i < strCurr.length(); i++) {
                    final int charActual = strCurr.charAt(i);
                    if (charActual != expectedResults[i]) {
                        System.err.println("<" + i + "> Actual = " + charActual
                                + " Expected = " + expectedResults[i]);
                        testPassed = false;
                        break;
                    }
                }
            }
        }
    }

    public static void setTaskStatus(boolean allTasksPerformed, int taskCount) {
        if (testPassed) {
            if (allTasksPerformed) {
                testResult = "All Tests Passed";
            } else {
                testResult = "Test " + Integer.toString(taskCount)
                        + " Passed";
            }
        } else {
            testResult = "Test " + Integer.toString(taskCount)
                    + " Failed";
        }
        lblTestStatus.setText(testResult);
    }
}
