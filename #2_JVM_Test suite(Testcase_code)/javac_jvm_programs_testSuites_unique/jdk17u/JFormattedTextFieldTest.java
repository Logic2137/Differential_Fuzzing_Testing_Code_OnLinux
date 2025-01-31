import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JFormattedTextFieldTest implements ActionListener {

    private static GridBagLayout layout;

    private static JPanel mainControlPanel;

    private static JPanel resultButtonPanel;

    private static JLabel instructionText;

    private static JButton passButton;

    private static JButton failButton;

    private static JFrame mainFrame;

    public static void main(String[] args) throws Exception {
        JFormattedTextFieldTest jFormattedTextFieldTest = new JFormattedTextFieldTest();
    }

    public JFormattedTextFieldTest() throws Exception {
        createUI();
    }

    public final void createUI() throws Exception {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf." + "windows.WindowsLookAndFeel");
        SwingUtilities.invokeAndWait(() -> {
            mainFrame = new JFrame("Window LAF JFormattedTextField Test");
            layout = new GridBagLayout();
            mainControlPanel = new JPanel(layout);
            resultButtonPanel = new JPanel(layout);
            GridBagConstraints gbc = new GridBagConstraints();
            String instructions = "<html>INSTRUCTIONS:<br>" + "Set Windows Theme to HighContrast#1.<br><br>" + "(ControlPanel->Personalization->High Contrast#1)<br><br>" + "If TextFiled colors are same test" + " passes else failed.<br><br></html>";
            instructionText = new JLabel();
            instructionText.setText(instructions);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mainControlPanel.add(instructionText, gbc);
            passButton = new JButton("Pass");
            passButton.setActionCommand("Pass");
            passButton.addActionListener(JFormattedTextFieldTest.this);
            failButton = new JButton("Fail");
            failButton.setActionCommand("Fail");
            failButton.addActionListener(JFormattedTextFieldTest.this);
            gbc.gridx = 0;
            gbc.gridy = 0;
            resultButtonPanel.add(passButton, gbc);
            gbc.gridx = 1;
            gbc.gridy = 0;
            resultButtonPanel.add(failButton, gbc);
            gbc.gridx = 3;
            gbc.gridy = 0;
            resultButtonPanel.add(new JTextField("12345"), gbc);
            NumberFormat format = NumberFormat.getIntegerInstance();
            format.setMaximumIntegerDigits(5);
            JFormattedTextField formatted = new JFormattedTextField(format);
            formatted.setText("67891");
            gbc.gridx = 5;
            gbc.gridy = 0;
            resultButtonPanel.add(formatted, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            mainControlPanel.add(resultButtonPanel, gbc);
            mainFrame.add(mainControlPanel);
            mainFrame.setSize(400, 200);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
            JButton btn = (JButton) evt.getSource();
            cleanUp();
            switch(btn.getActionCommand()) {
                case "Pass":
                    break;
                case "Fail":
                    throw new AssertionError("User Clicked Fail!");
            }
        }
    }

    private static void cleanUp() {
        mainFrame.dispose();
    }
}
