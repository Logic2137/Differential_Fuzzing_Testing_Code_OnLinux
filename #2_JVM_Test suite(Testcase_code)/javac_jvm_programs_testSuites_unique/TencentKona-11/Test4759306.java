



import javax.swing.JApplet;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class Test4759306 extends JApplet {
    public void init() {
        JColorChooser chooser = new JColorChooser();
        chooser.setPreviewPanel(new JPanel());
        getContentPane().add(chooser);
    }
}
