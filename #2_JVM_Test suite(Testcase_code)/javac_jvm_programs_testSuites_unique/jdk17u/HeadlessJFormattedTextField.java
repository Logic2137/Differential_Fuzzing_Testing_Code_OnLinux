import javax.swing.*;

public class HeadlessJFormattedTextField {

    public static void main(String[] args) {
        JTextField f = new JTextField("field");
        f.selectAll();
        f.getSelectionStart();
        f.getSelectionEnd();
        f.selectAll();
    }
}
