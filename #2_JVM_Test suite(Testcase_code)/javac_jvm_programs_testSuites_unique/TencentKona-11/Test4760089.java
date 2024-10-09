



import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Test4760089 extends JApplet {
    @Override
    public void init() {
        Border border = new EtchedBorder();
        border = new TitledBorder(border, "LEFT",  TitledBorder.LEFT,  TitledBorder.TOP);
        border = new TitledBorder(border, "RIGHT", TitledBorder.RIGHT, TitledBorder.TOP);

        JPanel panel = new JPanel();
        panel.setBorder(border);
        getContentPane().add(panel);
    }
}
