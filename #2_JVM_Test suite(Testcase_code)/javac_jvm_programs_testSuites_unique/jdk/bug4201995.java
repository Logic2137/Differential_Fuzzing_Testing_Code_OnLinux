



import javax.swing.*;

public class bug4201995 {
    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo LF :
                UIManager.getInstalledLookAndFeels()) {
            try {
                UIManager.setLookAndFeel(LF.getClassName());
            } catch (UnsupportedLookAndFeelException ignored) {
                System.out.println("Unsupported L&F: " + LF.getClassName());
                continue;
            } catch (ClassNotFoundException | InstantiationException
                     | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Testing L&F: " + LF.getClassName());
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    boolean expectedOpaqueValue =
                        !("Nimbus".equals(UIManager.getLookAndFeel().getName()) ||
                          UIManager.getLookAndFeel().getName().contains("GTK"));
                    JSplitPane sp = new JSplitPane();
                    System.out.println("sp.isOpaque " + sp.isOpaque());

                    if (sp.isOpaque() != expectedOpaqueValue) {
                        throw new RuntimeException("JSplitPane has incorrect default opaque value");
                    }
                }
            });
        }
    }
}
