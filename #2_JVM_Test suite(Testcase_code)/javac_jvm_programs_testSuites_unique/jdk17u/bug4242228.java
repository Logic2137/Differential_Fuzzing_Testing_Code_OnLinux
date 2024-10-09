import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;

public class bug4242228 {

    private static JTabbedPane tabPane;

    private static JFrame frame;

    public static void main(String[] argv) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame = new JFrame("4242228 Test");
                JScrollPane sourcePane = new JScrollPane();
                final JTextPane htmlEditor = new JTextPane();
                final JTextPane sourceEditor = new JTextPane();
                final JScrollPane editorPane = new JScrollPane();
                tabPane = new JTabbedPane();
                htmlEditor.setText(" ");
                htmlEditor.setEditorKit(new HTMLEditorKit());
                sourceEditor.setText(" ");
                sourceEditor.setEditorKit(new StyledEditorKit());
                frame.setLayout(new BorderLayout());
                editorPane.getViewport().add(htmlEditor);
                tabPane.addTab("Editor", editorPane);
                tabPane.addChangeListener(new ChangeListener() {

                    public void stateChanged(ChangeEvent e) {
                        if (tabPane.getSelectedComponent() == editorPane) {
                            htmlEditor.setText(sourceEditor.getText());
                        } else {
                            sourceEditor.setText(htmlEditor.getText());
                        }
                    }
                });
                sourcePane.getViewport().add(sourceEditor);
                tabPane.addTab("Source", sourcePane);
                tabPane.setTabPlacement(SwingConstants.BOTTOM);
                htmlEditor.setDocument(new HTMLDocument());
                frame.add(tabPane);
                frame.setSize(400, 300);
                frame.setVisible(true);
            }
        });
        Robot robot = new Robot();
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    tabPane.setSelectedIndex(i % 2);
                }
                frame.dispose();
            }
        });
    }
}
