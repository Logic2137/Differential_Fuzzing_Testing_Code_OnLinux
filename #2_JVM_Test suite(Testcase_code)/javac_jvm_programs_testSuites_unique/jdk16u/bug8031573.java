
import java.awt.FlowLayout;
import javax.swing.JApplet;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class bug8031573 extends JApplet {

    @Override
    public void init() {
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    JMenuBar bar = new JMenuBar();
                    JMenu menu = new JMenu("Menu");
                    JCheckBoxMenuItem checkBoxMenuItem
                            = new JCheckBoxMenuItem("JCheckBoxMenuItem");
                    checkBoxMenuItem.setSelected(true);
                    menu.add(checkBoxMenuItem);
                    bar.add(menu);
                    setJMenuBar(bar);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
