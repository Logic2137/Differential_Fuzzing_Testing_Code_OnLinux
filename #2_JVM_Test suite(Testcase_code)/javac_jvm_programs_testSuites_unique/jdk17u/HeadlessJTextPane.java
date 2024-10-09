import javax.swing.*;

public class HeadlessJTextPane {

    public static void main(String[] args) {
        JTextPane tp;
        tp = new JTextPane();
        tp.getEditorKit();
        tp.setContentType("text/html");
        tp.getContentType();
        tp.setText("Merry Parrot");
        tp.getText();
        tp.getDocument();
    }
}
