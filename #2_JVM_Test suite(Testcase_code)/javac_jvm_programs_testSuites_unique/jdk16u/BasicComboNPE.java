


import javax.swing.SwingUtilities;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.ComboBoxModel;
import java.awt.IllegalComponentStateException;

public class BasicComboNPE extends JComboBox
{
    public static void main(String[] args) {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            try {
                System.out.println("Test for LookAndFeel " + laf.getClassName());
                UIManager.setLookAndFeel(laf.getClassName());
                new BasicComboNPE().getModel();
            } catch (IllegalComponentStateException | ClassNotFoundException | InstantiationException |
                     IllegalAccessException | UnsupportedLookAndFeelException e ) {
               
            }
        }

    }

    @Override
    public ComboBoxModel getModel()
    {
        setPopupVisible(false);
        isPopupVisible();
        return super.getModel();
    }
}
