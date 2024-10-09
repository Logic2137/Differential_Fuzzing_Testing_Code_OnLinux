import java.awt.Font;
import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

public class bug4174551 extends JApplet {

    public void init() {
        try {
            java.awt.EventQueue.invokeLater(() -> {
                UIManager.getDefaults().put("OptionPane.buttonFont", new Font("Dialog", Font.PLAIN, 10));
                UIManager.getDefaults().put("OptionPane.messageFont", new Font("Dialog", Font.PLAIN, 24));
                JOptionPane.showMessageDialog(null, "HI 24!");
                System.out.println(UIManager.getDefaults().get("OptionPane.buttonFont"));
                System.out.println(UIManager.getDefaults().get("OptionPane.messageFont"));
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
