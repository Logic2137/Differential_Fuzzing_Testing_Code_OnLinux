



import javax.swing.*;
import java.applet.Applet;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.locks.LockSupport;

public class SpanishDiacriticsTest extends Applet {
    @Override
    public void init() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            JTextField textField = new JTextField(20);
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    LockSupport.parkNanos(1_000_000_000L);
                }
            });
            frame.add(textField);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}

