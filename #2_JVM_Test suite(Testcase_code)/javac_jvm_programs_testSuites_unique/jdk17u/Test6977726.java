import javax.swing.JApplet;
import javax.swing.JColorChooser;
import javax.swing.JLabel;

public class Test6977726 extends JApplet {

    public void init() {
        JColorChooser chooser = new JColorChooser();
        chooser.setPreviewPanel(new JLabel("Text Preview Panel"));
        getContentPane().add(chooser);
    }
}
