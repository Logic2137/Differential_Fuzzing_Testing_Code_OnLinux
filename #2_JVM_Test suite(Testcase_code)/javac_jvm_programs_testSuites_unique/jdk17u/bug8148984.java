import javax.swing.*;

public class bug8148984 extends JApplet {

    @Override
    public void init() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Text field:"));
                panel.add(new JTextField(20));
                add(panel);
            }
        });
    }
}
