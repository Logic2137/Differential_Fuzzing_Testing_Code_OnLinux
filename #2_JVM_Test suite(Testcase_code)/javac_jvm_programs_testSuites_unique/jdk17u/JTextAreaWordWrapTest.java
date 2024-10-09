import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class JTextAreaWordWrapTest {

    static JFrame frame;

    static JFrame frame1;

    static JTextArea textArea;

    static JTextArea textArea1;

    public static void doWrapOnTest() {
        frame = new JFrame();
        frame.setSize(720, 300);
        frame.setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 100; i++) {
            sb.append("zz zzz zzzz zz zz zz zzz xzzzz zzzzzzzzzzzzzzzzx yyyyyyy tttttttttt sssss hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\n");
        }
        textArea.setText(sb.toString());
        JScrollPane pane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.add(pane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void doWrapOffTest() {
        frame1 = new JFrame();
        frame1.setSize(720, 300);
        frame1.setLayout(new BorderLayout());
        textArea1 = new JTextArea();
        StringBuffer sb1 = new StringBuffer();
        for (int i = 0; i < 100; i++) {
            sb1.append("zz zzz zzzz zz zz zz zzz xzzzz zzzzzzzzzzzzzzzzx yyyyyyy tttttttttt sssss hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\n");
        }
        textArea1.setText(sb1.toString());
        JScrollPane pane1 = new JScrollPane(textArea1, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame1.add(pane1, BorderLayout.CENTER);
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("sun.java2d.uiScale", "1.25");
        try {
            SwingUtilities.invokeAndWait(() -> doWrapOnTest());
            Thread.sleep(500);
            SwingUtilities.invokeAndWait(() -> doWrapOffTest());
            Thread.sleep(500);
            SwingUtilities.invokeAndWait(() -> {
                int wraponHeight = textArea.getHeight();
                System.out.println("wraponheight " + wraponHeight);
                int wrapoffHeight = textArea1.getHeight();
                System.out.println("wrapoffheight " + wrapoffHeight);
                if (wraponHeight == wrapoffHeight) {
                    throw new RuntimeException("JTextArea line wrapping incorrect when using UI scale");
                }
            });
        } finally {
            SwingUtilities.invokeAndWait(() -> frame.dispose());
            SwingUtilities.invokeAndWait(() -> frame1.dispose());
        }
    }
}
