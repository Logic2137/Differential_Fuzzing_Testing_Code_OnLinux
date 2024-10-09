import javax.swing.*;

public class bug4201995 {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                boolean expectedOpaqueValue = !"Nimbus".equals(UIManager.getLookAndFeel().getName());
                JSplitPane sp = new JSplitPane();
                if (sp.isOpaque() != expectedOpaqueValue) {
                    throw new RuntimeException("JSplitPane has incorrect default opaque value");
                }
            }
        });
    }
}
