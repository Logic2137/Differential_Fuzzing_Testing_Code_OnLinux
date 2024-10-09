

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.UIResource;

import static javax.swing.JSpinner.*;
import static javax.swing.UIManager.getInstalledLookAndFeels;


public class FontSetByUser implements Runnable {

    private static final Font USERS_FONT = new Font("dialog", Font.BOLD, 41);

    public static void main(final String[] args) throws Exception {
        for (final UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
            SwingUtilities.invokeAndWait(new FontSetByUser());
        }
    }

    @Override
    public void run() {
        final JFrame frame1 = new JFrame();
        try {
            testDefaultFont(frame1);
        } finally {
            frame1.dispose();
        }
    }

    private static void testDefaultFont(final JFrame frame) {
        final JSpinner spinner = new JSpinner();
        final JSpinner spinner_u = new JSpinner();
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
        frame.getContentPane().add(spinner);
        frame.getContentPane().add(spinner_u);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        final DefaultEditor ed = (DefaultEditor) spinner.getEditor();
        final DefaultEditor ed_u = (DefaultEditor) spinner_u.getEditor();
        ed_u.getTextField().setFont(USERS_FONT);

        for (int i = 5; i < 40; i += 5) {
            
            final Font tff = ed.getTextField().getFont();
            if (!(tff instanceof UIResource)) {
                throw new RuntimeException("Font must be UIResource");
            }
            if (spinner.getFont().getSize() != tff.getSize()) {
                throw new RuntimeException("Rrong size");
            }
            spinner.setFont(new Font("dialog", Font.BOLD, i));
            
            final Font tff_u = ed_u.getTextField().getFont();
            if (tff_u instanceof UIResource || !tff_u.equals(USERS_FONT)) {
                throw new RuntimeException("Font must NOT be UIResource");
            }
            if (spinner_u.getFont().getSize() == tff_u.getSize()) {
                throw new RuntimeException("Wrong size");
            }
            spinner_u.setFont(new Font("dialog", Font.BOLD, i));
        }
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
            System.out.println("LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
