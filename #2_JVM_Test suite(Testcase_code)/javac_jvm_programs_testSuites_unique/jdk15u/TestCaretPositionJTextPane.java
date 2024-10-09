


import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

public class TestCaretPositionJTextPane {
    private static JTextPane textPane;
    private static JFrame f;

    private static void createUI() {
        f = new JFrame("Test Cursor/Caret with Java 9");

        textPane = new JTextPane();
        textPane.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));

        fillTextPane(textPane);

        textPane.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    Caret caret = textPane.getCaret();
                    Rectangle2D rect = textPane.modelToView2D(caret.getDot());

                    if (Math.abs(e.getPoint().x - rect.getX()) > 5) {
                        System.out.println("mouse point " + e.getPoint());
                        System.out.println("caret position " + rect);
                        throw new RuntimeException(" Wrong caret position");
                    }
                } catch (BadLocationException ex) {}
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
        f.add(new JScrollPane(textPane), BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }

    public static void main(String args[]) throws Exception {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(200);

            SwingUtilities.invokeAndWait(() -> createUI());

            robot.waitForIdle();
            Point p = textPane.getLocationOnScreen();
            robot.mouseMove(p.x+ 480, p.y+6);
            robot.waitForIdle();
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();
        } finally {
             SwingUtilities.invokeAndWait(() -> f.dispose());
        }
    }

    private static void fillTextPane(JTextPane textPane) {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < 30; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < 50; j++) {
                row.append(j);
                if (j % 5 == 0) {
                    row.append(" ");
                }
            }
            buf.append(row).append(System.lineSeparator());
        }
        textPane.setText(buf.toString());
        textPane.setCaretPosition(0);
    }
}
