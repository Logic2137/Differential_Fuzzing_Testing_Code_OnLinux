



import javax.swing.*;


public class bug6882559 {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Thread.currentThread().setContextClassLoader(null);
            new javax.swing.JEditorPane("text/plain", "");
        });
    }
}
