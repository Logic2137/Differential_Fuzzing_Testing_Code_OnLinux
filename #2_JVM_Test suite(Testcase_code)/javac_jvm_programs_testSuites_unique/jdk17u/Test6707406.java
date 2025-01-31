import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JColorChooser;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.basic.BasicColorChooserUI;

public class Test6707406 extends BasicColorChooserUI implements PropertyChangeListener {

    public static void main(String[] args) throws Exception {
        test();
        for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            System.out.println(laf.getName());
            UIManager.setLookAndFeel(laf.getClassName());
            test();
        }
    }

    private static void test() {
        JColorChooser chooser = new JColorChooser();
        chooser.getUI().uninstallUI(chooser);
        new Test6707406().installUI(chooser);
        chooser.getSelectionModel().setSelectedColor(Color.BLUE);
    }

    @Override
    protected PropertyChangeListener createPropertyChangeListener() {
        return this;
    }

    public void propertyChange(PropertyChangeEvent event) {
    }
}
