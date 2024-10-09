
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;


public class bug8058305 {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(bug8058305::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();

        JFormattedTextField textField = new JFormattedTextField();
        NavigationFilter navigationFilter = new NavigationFilter();
        textField.setText("Test for Tests");
        frame.getContentPane().add(textField);
        frame.pack();

        Position.Bias[] biasRet = {Position.Bias.Forward};
        try {
            navigationFilter.getNextVisualPositionFrom(textField, 100,
                    Position.Bias.Backward, SwingConstants.EAST, biasRet);
            throw new RuntimeException("BadLocationException is not thrown!");
        } catch (BadLocationException expectedException) {
        }

        frame.setVisible(true);

        try {
            navigationFilter.getNextVisualPositionFrom(textField, 200,
                    Position.Bias.Forward, SwingConstants.WEST, biasRet);
            throw new RuntimeException("BadLocationException is not thrown!");
        } catch (BadLocationException expectedException) {
        }
    }
}
