import javax.swing.*;

public class HeadlessJPasswordField {

    public static void main(String[] args) {
        JPasswordField f = new JPasswordField("field");
        f.selectAll();
        f.getSelectionStart();
        f.getSelectionEnd();
    }
}
