



import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.GraphicsDevice.WindowTranslucency.*;

public class bug7160604 extends JApplet {

    public void init() {
        SwingUtilities.invokeLater(() -> {
            if (!GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT)) {
                
                return;
            }

            final JWindow window = new JWindow();
            window.setLocation(200, 200);
            window.setSize(300, 300);

            final JLabel label = new JLabel("...click to invoke JPopupMenu");
            label.setOpaque(true);
            final JPanel contentPane = new JPanel(new BorderLayout());
            contentPane.setBorder(BorderFactory.createLineBorder(Color.RED));
            window.setContentPane(contentPane);
            contentPane.add(label, BorderLayout.NORTH);

            final JComboBox comboBox = new JComboBox(new Object[]{"1", "2", "3", "4"});
            contentPane.add(comboBox, BorderLayout.SOUTH);

            final JPopupMenu jPopupMenu = new JPopupMenu();

            jPopupMenu.add("string");
            jPopupMenu.add(new AbstractAction("action") {
                @Override
                public void actionPerformed(final ActionEvent e) {
                }
            });
            jPopupMenu.add(new JLabel("label"));
            jPopupMenu.add(new JMenuItem("MenuItem"));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(final MouseEvent e) {
                    jPopupMenu.show(label, 0, 0);
                }
            });

            window.setBackground(new Color(0, 0, 0, 0));

            window.setVisible(true);
        });
    }
}
