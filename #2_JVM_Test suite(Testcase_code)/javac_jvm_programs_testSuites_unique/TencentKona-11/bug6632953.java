


import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalComboBoxUI;

public class bug6632953 {

    public static void main(String... args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {

                for (UIManager.LookAndFeelInfo lafInfo
                        : UIManager.getInstalledLookAndFeels()) {
                    try {
                        UIManager.setLookAndFeel(lafInfo.getClassName());
                    } catch (UnsupportedLookAndFeelException ignored) {
                        continue;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    MetalComboBoxUI ui = new MetalComboBoxUI();
                    ui.installUI(new JComboBox());
                    ui.getBaseline(new JComboBox(), 0, 0);
                    ui.getBaseline(new JComboBox(), 1, 1);
                    ui.getBaseline(new JComboBox(), 2, 2);
                    ui.getBaseline(new JComboBox(), 3, 3);
                    ui.getBaseline(new JComboBox(), 4, 4);
                }
            }
        });
    }
}
