

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


public class bug8034955 {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.getContentPane().add(new JLabel("<html>a<title>"));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
