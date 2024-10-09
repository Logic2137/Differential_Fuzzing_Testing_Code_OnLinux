



import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.UIResource;
import static javax.swing.UIManager.getInstalledLookAndFeels;

public class FontByDefault implements Runnable {

    public static void main(final String[] args) throws Exception {
        for (final UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
            SwingUtilities.invokeAndWait(new FontByDefault());
        }
    }

    @Override
    public void run() {
        final JFrame mainFrame = new JFrame();
        try {
            testDefaultFont(mainFrame);
        } finally {
            mainFrame.dispose();
        }
    }

    private static void testDefaultFont(final JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JSpinner spinner = new JSpinner();
        frame.add(spinner);
        frame.setSize(300, 100);
        frame.setVisible(true);

        final DefaultEditor editor = (DefaultEditor) spinner.getEditor();
        final Font editorFont = editor.getTextField().getFont();

        

        if (!(editorFont instanceof UIResource)) {
            throw new RuntimeException("Font must be UIResource");
        }
        if (!editorFont.equals(spinner.getFont())) {
            throw new RuntimeException("Wrong FONT");
        }
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
