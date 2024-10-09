import java.awt.Color;
import java.awt.Font;
import javax.swing.JApplet;
import javax.swing.JColorChooser;
import javax.swing.UIManager;

public class Test4887836 extends JApplet {

    public void init() {
        UIManager.put("Label.font", new Font("Perpetua", 0, 36));
        add(new JColorChooser(Color.LIGHT_GRAY));
    }
}
