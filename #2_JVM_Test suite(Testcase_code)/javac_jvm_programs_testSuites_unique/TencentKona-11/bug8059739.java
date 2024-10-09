



import javax.swing.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class bug8059739 {

    private static boolean passed = true;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    runTest();
                } catch (Exception e) {
                    e.printStackTrace();
                    passed = false;
                }
            }
        });

        if (!passed) {
            throw new RuntimeException("Test FAILED.");
        } else {
            System.out.println("Passed.");
        }
    }

    private static void runTest() throws Exception {
        String testString = "my string";
        JTextField tf = new JTextField(testString);
        tf.selectAll();
        Clipboard clipboard = new Clipboard("clip");
        tf.getTransferHandler().exportToClipboard(tf, clipboard, TransferHandler.COPY);
        DataFlavor[] dfs = clipboard.getAvailableDataFlavors();
        for (DataFlavor df: dfs) {
            String charset = df.getParameter("charset");
            if (InputStream.class.isAssignableFrom(df.getRepresentationClass()) &&
                    charset != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (InputStream) clipboard.getData(df), charset));
                String s = br.readLine();
                System.out.println("Content: '" + s + "'");
                passed &= s.contains(testString);
            }
        }
    }
}
