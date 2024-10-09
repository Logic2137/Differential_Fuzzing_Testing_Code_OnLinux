import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

public class I18NViewNoWrapMinSpan {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            try {
                JTextField textField = new JTextField(15);
                textField.setText("\u0E2112345");
                float noSpaceMin = textField.getUI().getRootView(textField).getMinimumSpan(0);
                textField.getDocument().insertString(3, " ", null);
                if (noSpaceMin > textField.getUI().getRootView(textField).getMinimumSpan(0)) {
                    throw new RuntimeException("Minimum span is calculated for wrapped text");
                }
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("ok");
    }
}
