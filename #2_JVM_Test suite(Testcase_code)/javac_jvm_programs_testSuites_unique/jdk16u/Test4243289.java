



import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class Test4243289 extends JApplet {
    public void init() {
        Font font = new Font("Dialog", Font.PLAIN, 12); 
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Panel Title", 
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                font);

        JPanel panel = new JPanel();
        panel.setBorder(border);
        getContentPane().add(panel);
    }
}
