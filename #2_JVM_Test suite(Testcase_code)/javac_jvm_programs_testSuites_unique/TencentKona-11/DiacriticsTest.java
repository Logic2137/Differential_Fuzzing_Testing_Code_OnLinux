





import java.applet.Applet;
import java.awt.*;
import javax.swing.JPanel;


public class DiacriticsTest extends Applet {

    public void init() {
        this.setLayout(new BorderLayout());
    }

    public void start() {

        setSize(350, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        TextArea txtArea = new TextArea();
        panel.add(txtArea);

        TextField txtField = new TextField();
        panel.add(txtField);

        add(panel, BorderLayout.CENTER);

        validate();
        setVisible(true);
    }
}

