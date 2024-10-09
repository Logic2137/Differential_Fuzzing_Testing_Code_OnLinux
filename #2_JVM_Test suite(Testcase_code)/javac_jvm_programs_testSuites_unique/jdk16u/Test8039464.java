

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;



public class Test8039464 extends JApplet {
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            throw new Error("unexpected", exception);
        }
    }

    @Override
    public void init() {
        init(this);
    }

    private static void init(Container container) {
        container.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel label = new JLabel();
        Dimension size = new Dimension(111, 0);
        label.setPreferredSize(size);
        label.setMinimumSize(size);
        container.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        container.add(new JScrollBar(JScrollBar.HORIZONTAL, 1, 111, 1, 1111), gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        container.add(new JScrollBar(JScrollBar.VERTICAL, 1, 111, 1, 1111), gbc);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("8039464");
                init(frame);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
