import javax.swing.*;
import java.awt.*;

public class bug7082443 {

    public static final String GTK_LAF_CLASS = "GTKLookAndFeel";

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            if (lookAndFeelInfo.getClassName().contains(GTK_LAF_CLASS)) {
                try {
                    UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
                } catch (final UnsupportedLookAndFeelException ignored) {
                    continue;
                }
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        TestComboBox testComboBox = new TestComboBox();
                        if (testComboBox.isOldRendererOpaque()) {
                            System.out.println("Passed for " + GTK_LAF_CLASS);
                        } else {
                            throw new RuntimeException("Failed for " + GTK_LAF_CLASS);
                        }
                    }
                });
                return;
            }
        }
        System.out.println(GTK_LAF_CLASS + " is not found. The test skipped");
    }

    private static class TestComboBox extends JComboBox {

        private final ListCellRenderer renderer = new ListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return TestComboBox.super.getRenderer().getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        };

        @Override
        public ListCellRenderer getRenderer() {
            return renderer;
        }

        public boolean isOldRendererOpaque() {
            return ((JLabel) super.getRenderer()).isOpaque();
        }
    }
}
