



import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;

public class bug8058120 {
    private static HTMLDocument document = null;
    private static final String text = "<p id = 'ab'>ab</p>";
    private static final String textToInsert = "c";

    public static void main(String[] args) {
        Robot robot;
        try {
             robot = new Robot();
        }catch(Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unexpected failure");
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });

        robot.waitForIdle();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    document.insertAfterEnd(document.getElement("ab"), textToInsert);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        robot.waitForIdle();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Element parent = document.getElement("ab").getParentElement();
                int count = parent.getElementCount();
                if (count != 2) {
                    throw new RuntimeException("Test Failed! Unexpected Element count = "+count);
                }
                Element insertedElement = parent.getElement(count - 1);
                if (!HTML.Tag.IMPLIED.toString().equals(insertedElement.getName())) {
                    throw new RuntimeException("Test Failed! Inserted text is not wrapped by " + HTML.Tag.IMPLIED + " tag");
                }
            }
        });
    }

    private static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        JFrame frame = new JFrame("bug8058120");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditorKit(new HTMLEditorKit());

        document = (HTMLDocument) editorPane.getDocument();

        editorPane.setText(text);

        frame.add(editorPane);
        frame.setSize(200, 200);
        frame.setVisible(true);
    }
}
