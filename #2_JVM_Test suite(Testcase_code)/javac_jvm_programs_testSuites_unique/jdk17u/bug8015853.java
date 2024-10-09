import java.io.*;
import java.net.URL;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;

public class bug8015853 {

    private static String text = "";

    public static void main(String[] args) throws Exception {
        try {
            URL path = ClassLoader.getSystemResource("bug8015853.txt");
            File file = new File(path.toURI());
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                text += scanner.nextLine() + "\n";
            }
            scanner.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        text += text;
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JEditorPane editorPane = new JEditorPane();
        HTMLEditorKit editorKit = new HTMLEditorKit();
        editorPane.setEditorKit(editorKit);
        editorPane.setText(text);
        frame.add(editorPane);
        frame.setVisible(true);
    }
}
