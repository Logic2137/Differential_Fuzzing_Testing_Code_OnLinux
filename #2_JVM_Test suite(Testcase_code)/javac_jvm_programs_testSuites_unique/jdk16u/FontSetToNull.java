

import java.awt.Font;

import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.UIResource;

import static javax.swing.JSpinner.DefaultEditor;
import static javax.swing.UIManager.getInstalledLookAndFeels;


public final class FontSetToNull {

    private static final Font USERS_FONT = new Font("dialog", Font.BOLD, 41);

    public static void main(final String[] args) throws Exception {
        for (final UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
            SwingUtilities.invokeAndWait(() -> {
                
                test(new JSpinner());
                
                test(new JSpinner(){
                    @Override
                    public Font getFont() {
                        return null;
                    }
                });
            });
        }
    }

    
    private static void test(JSpinner spinner) {
        final DefaultEditor de = (DefaultEditor) spinner.getEditor();

        spinner.setFont(null); 
        SwingUtilities.updateComponentTreeUI(de); 
        spinner.setFont(null); 

        
        de.getTextField().setFont(USERS_FONT);
        spinner.setFont(null);

        final Font tff = de.getTextField().getFont();
        if (tff instanceof UIResource || !tff.equals(USERS_FONT)) {
            throw new RuntimeException("Wrong font: " + tff);
        }

        spinner.setEditor(new JSpinner().getEditor()); 
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
