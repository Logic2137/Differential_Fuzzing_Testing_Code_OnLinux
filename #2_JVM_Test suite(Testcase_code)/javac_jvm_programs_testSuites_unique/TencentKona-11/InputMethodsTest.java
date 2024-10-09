





import java.applet.Applet;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class InputMethodsTest extends Applet {

    TextArea txtArea = null;
    TextField txtField = null;
    JButton btnIM = null;
    boolean inputMethodsEnabled = true;

    public void init() {
        this.setLayout(new BorderLayout());
    }

    public void start() {

        setSize(350, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        txtArea = new TextArea();
        panel.add(txtArea);

        txtField = new TextField();
        panel.add(txtField);

        add(panel, BorderLayout.CENTER);

        btnIM = new JButton();
        setBtnText();

        btnIM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputMethodsEnabled = !inputMethodsEnabled;
                setBtnText();
                txtArea.enableInputMethods(inputMethodsEnabled);
                txtField.enableInputMethods(inputMethodsEnabled);
            }
        });

        add(btnIM, BorderLayout.SOUTH);

        validate();
        setVisible(true);
    }

    private void setBtnText() {
        String s = inputMethodsEnabled ? "Disable" : "Enable";
        btnIM.setText(s +  " Input Methods");
    }
}

