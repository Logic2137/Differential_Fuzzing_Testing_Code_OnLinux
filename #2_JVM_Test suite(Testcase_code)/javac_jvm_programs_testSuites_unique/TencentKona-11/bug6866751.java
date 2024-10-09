



import javax.swing.*;
import java.awt.*;

public class bug6866751 {
    private static JFrame frame;
    private static JTextArea area;

    public static void main(String[] args) throws Exception {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    frame = new JFrame();
                    frame.setUndecorated(true);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    setup(frame);
                }
            });
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    int width = area.getWidth();
                    double caretX =
                            area.getCaret().getMagicCaretPosition().getX();
                    if (width < caretX + 1) {
                        throw new RuntimeException(
                                "Width of the area (" + width +
                                        ") is less than caret x-position " +
                                        caretX + 1);
                    }
                    area.putClientProperty("caretWidth", 10);
                    frame.pack();
                }
            });
            new Robot().waitForIdle();
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    int width = area.getWidth();
                    double caretX =
                            area.getCaret().getMagicCaretPosition().getX();
                    if (width < caretX + 10) {
                        throw new RuntimeException(
                                "Width of the area (" + width +
                                        ") is less  than caret x-position " +
                                        caretX + 10);
                    }
                }
            });
            System.out.println("ok");
        } finally {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    if (frame != null) { frame.dispose(); }
                }
            });
        }
    }

    static void setup(JFrame frame) {
        area = new JTextArea();
        frame.getContentPane().add(new JScrollPane(area));
        area.setText(
                "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
        area.getCaret().setDot(area.getText().length() + 1);

        frame.setSize(300, 200);
        frame.setVisible(true);

        area.requestFocus();

    }

}
