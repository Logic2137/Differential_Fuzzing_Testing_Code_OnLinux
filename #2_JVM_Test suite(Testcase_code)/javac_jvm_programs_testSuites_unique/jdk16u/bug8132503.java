



import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class bug8132503 extends JApplet {
    @Override
    public void init() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JTextArea textArea = new JTextArea("Text area of the test.", 40, 40);
                add(new JScrollPane(textArea));
            }
        });
    }
}
