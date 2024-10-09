import javax.swing.*;
import java.awt.*;

public class bug8001470 {

    private static JFrame frame;

    private static JTextField textField1;

    private static JTextField textField2;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame = new JFrame("JTextField Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel container = (JPanel) frame.getContentPane();
                container.setLayout(new GridLayout(2, 1));
                textField1 = new JTextField("\u0e01");
                textField2 = new JTextField("\u0c01");
                container.add(textField1);
                container.add(textField2);
                frame.setVisible(true);
                frame.pack();
            }
        });
        if (textField1.getHeight() < 10 || textField2.getHeight() < 10)
            throw new Exception("Wrong field height");
        System.out.println("ok");
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.dispose();
            }
        });
    }
}
