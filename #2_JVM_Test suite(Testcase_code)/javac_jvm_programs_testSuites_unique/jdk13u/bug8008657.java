import java.awt.ComponentOrientation;
import java.awt.Robot;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class bug8008657 {

    private static Robot robot;

    private static JSpinner spinner;

    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        UIManager.LookAndFeelInfo[] lookAndFeelArray = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo lookAndFeelItem : lookAndFeelArray) {
            executeCase(lookAndFeelItem.getClassName());
        }
    }

    static void executeCase(String lookAndFeelString) throws Exception {
        if (tryLookAndFeel(lookAndFeelString)) {
            SwingUtilities.invokeAndWait(() -> {
                createDateSpinner();
                createAndShowUI();
            });
            robot.waitForIdle();
            testSpinner(false);
            cleanUp();
            SwingUtilities.invokeAndWait(() -> {
                createNumberSpinner();
                createAndShowUI();
            });
            robot.waitForIdle();
            testSpinner(true);
            cleanUp();
        }
    }

    static void testSpinner(boolean checkHorizontalAligment) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            spinner.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> {
            JTextField textField = getTextField();
            if (!ComponentOrientation.RIGHT_TO_LEFT.equals(textField.getComponentOrientation())) {
                throw new RuntimeException("Wrong orientation!");
            }
            if (checkHorizontalAligment && textField.getHorizontalAlignment() != JTextField.LEFT) {
                throw new RuntimeException("Wrong horizontal aligment!");
            }
            spinner.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> {
            JTextField textField = getTextField();
            if (!ComponentOrientation.LEFT_TO_RIGHT.equals(textField.getComponentOrientation())) {
                throw new RuntimeException("Wrong orientation!");
            }
            if (checkHorizontalAligment && textField.getHorizontalAlignment() != JTextField.RIGHT) {
                throw new RuntimeException("Wrong horizontal aligment!");
            }
            spinner.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        });
    }

    static JTextField getTextField() {
        return ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
    }

    static String getOrientation(ComponentOrientation orientation) {
        return orientation == ComponentOrientation.LEFT_TO_RIGHT ? "LEFT_TO_RIGHT" : "RIGHT_TO_LEFT";
    }

    static void createDateSpinner() {
        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -1);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        Date latestDate = calendar.getTime();
        SpinnerModel dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
        spinner = new JSpinner();
        spinner.setModel(dateModel);
    }

    static void createNumberSpinner() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        calendar.add(Calendar.YEAR, 1);
        int currentYear = calendar.get(Calendar.YEAR);
        SpinnerModel yearModel = new 
        SpinnerNumberModel(currentYear, currentYear - 1, currentYear + 2, 1);
        spinner = new JSpinner();
        spinner.setModel(yearModel);
    }

    static void createAndShowUI() {
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.getContentPane().add(spinner);
        frame.setVisible(true);
    }

    private static void cleanUp() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.dispose();
            }
        });
    }

    private static boolean tryLookAndFeel(String lookAndFeelString) throws Exception {
        try {
            UIManager.setLookAndFeel(lookAndFeelString);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            return false;
        }
        return true;
    }
}
