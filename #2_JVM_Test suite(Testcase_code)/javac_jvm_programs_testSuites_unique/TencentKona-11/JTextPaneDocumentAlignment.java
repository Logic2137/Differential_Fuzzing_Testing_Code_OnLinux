



import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class JTextPaneDocumentAlignment {

    private static JFrame frame;
    private static JTextPane jTextPane;
    private static int position;

    public static void main(String[] args) throws Exception{
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame();
                frame.setUndecorated(true);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setSize(200, 200);
                jTextPane = new JTextPane();
                jTextPane.setContentType("text/html");
                jTextPane.setText(
                        "<html><body><b id='test'>Test</b></body></html>");
                SimpleAttributeSet right = new SimpleAttributeSet();
                StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
                jTextPane.getStyledDocument()
                        .setParagraphAttributes(0, 10, right, true);
                frame.getContentPane().add(jTextPane);
                frame.setVisible(true);
            }
        });
        Robot robot = new Robot();
        robot.waitForIdle();
        robot.delay(200);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    position = jTextPane.modelToView(1).x;
                    SimpleAttributeSet center = new SimpleAttributeSet();
                    StyleConstants.setAlignment(center,
                            StyleConstants.ALIGN_CENTER);
                    jTextPane.getStyledDocument()
                            .setParagraphAttributes(0, 10, center, true);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });
        if(position < 100) {
            throw  new RuntimeException("Text is not right aligned " + position);
        }
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    position = jTextPane.modelToView(1).x;
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                frame.dispose();
            }
        });
        if(position < 20) {
            throw  new RuntimeException("Text is not center aligned " + position);
        }
        System.out.println("ok");
    }
}
