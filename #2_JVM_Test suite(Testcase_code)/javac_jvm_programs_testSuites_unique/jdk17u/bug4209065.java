import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

public final class bug4209065 extends JApplet {

    @Override
    public void init() {
        try {
            EventQueue.invokeAndWait(this::createTabbedPane);
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTabbedPane() {
        JTabbedPane tp = new JTabbedPane();
        getContentPane().add(tp);
        String text = "<html><center>If the style of the text on the tabs matches" + "<br>the descriptions, press <em><b>PASS</b></em></center></html>";
        tp.addTab("<html><center><font size=+3>big</font></center></html>", new JLabel(text));
        tp.addTab("<html><center><font color=red>red</font></center></html>", new JLabel(text));
        tp.addTab("<html><center><em><b>Bold Italic!</b></em></center></html>", new JLabel(text));
    }
}
