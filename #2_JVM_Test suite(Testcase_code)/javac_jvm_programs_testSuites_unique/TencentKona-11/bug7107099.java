



import javax.swing.*;
import java.awt.*;

public class bug7107099 {
    private static JFrame frame;
    private static JTextArea textarea;
    private static JScrollPane scrollPane;

    private static int value;
    private static int min;
    private static int max;
    private static int extent;

    public static void main(String[] args) throws Exception {

        java.awt.Robot robot = new java.awt.Robot();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                textarea = new JTextArea("before###1###\nbefore###2###\nbefore###3###\nbefore###4###\nbefore###5###\n");

                scrollPane = new JScrollPane(textarea);
                scrollPane.setPreferredSize(new Dimension(100, 50));

                frame = new JFrame();
                frame.setLayout(new BorderLayout());
                frame.setSize(200, 200);
                frame.add(scrollPane, BorderLayout.SOUTH);
                frame.setVisible(true);
            }
        });

        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                BoundedRangeModel model = scrollPane.getVerticalScrollBar().getModel();

                value = model.getValue();
                min = model.getMinimum();
                max = model.getMaximum();
                extent = model.getExtent();

                
                textarea.setText(null);
                scrollPane.setViewportView(textarea);
                textarea.setText("after###1###\nafter###1###\nafter###1###\nafter###1###\nafter###1###\n");
                textarea.setCaretPosition(0);
            }
        });

        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                BoundedRangeModel model = scrollPane.getVerticalScrollBar().getModel();

                if (value != model.getValue() ||
                        min != model.getMinimum() ||
                        max != model.getMaximum() ||
                        extent != model.getExtent()) {
                    throw new RuntimeException("Test bug7107099 failed");
                }

                System.out.println("Test bug7107099 passed");

                frame.dispose();
            }
        });
    }
}
