





import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.MouseEvent;

public class bug6938583 {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JTextArea jta = new JTextArea();
                DefaultCaret dc = new DefaultCaret();
                jta.setCaret(dc);
                dc.deinstall(jta);
                dc.mouseClicked(new MouseEvent(jta, MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, 0, false));
            }
        });
    }
}
