

import javax.swing.*;
import javax.swing.text.*;


public class DocumentInsertAtWrongPositionTest {
    public static void main(String[] args) throws Exception {
        JTextField te = new JTextField("1234567890");
        JTextPane tp = new JTextPane();
        tp.setText("1234567890");
        JTextArea ta = new JTextArea("1234567890");

        try {
            ta.insert("abc", 11);

            throw new RuntimeException("failed");
        } catch (IllegalArgumentException e) {
        }
        try {

            te.getDocument().insertString(11, "abc", new SimpleAttributeSet());

            throw new RuntimeException("failed");
        } catch (BadLocationException e) {
        }
        try {
            tp.getDocument().insertString(11, "abc", new SimpleAttributeSet());
            throw new RuntimeException("failed");
        } catch (BadLocationException e) {
        }
    }
}
