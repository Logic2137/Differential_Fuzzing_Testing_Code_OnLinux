



import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.CSS;
import java.awt.*;

public class JTextPaneDocumentWrapping {

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
                        "<html><body><b id='test'>Test Test Test Test Test Test " +
                                "Test Test Test Test Test Test Test Test Test Test " +
                                "Test Test Test Test Test Test Test Test Test Test" +
                                "</b></body></html>");
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
                    position = jTextPane.modelToView(100).y;
                    SimpleAttributeSet wrap = new SimpleAttributeSet();
                    wrap.addAttribute(CSS.Attribute.WHITE_SPACE, "nowrap");
                    jTextPane.getStyledDocument()
                            .setParagraphAttributes(0, 10, wrap, true);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });
        if(position < 40) {
            throw  new RuntimeException("Text is not wrapped " + position);
        }
        robot.waitForIdle();
        robot.delay(200);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    position = jTextPane.modelToView(100).y;
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                frame.dispose();
            }
        });
        if(position > 20) {
            throw  new RuntimeException("Text is wrapped " + position);
        }
        System.out.println("ok");

    }
}
