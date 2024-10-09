



import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LCDTextAndGraphicsState extends Component {

    private static final Frame testFrame = new Frame("Composite and Text Test");
    private static final String text = "This test passes only if this text appears SIX TIMES else fail";
    private static volatile boolean testResult;
    private static volatile CountDownLatch countDownLatch;

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setColor(Color.white);
        g2d.fillRect(0,0,getSize().width, getSize().height);
        test1(g.create(0, 0, 500, 100));
        test2(g.create(0, 100, 500, 100));
        test3(g.create(0, 200, 500, 100));
    }

    public void test1(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setColor(Color.black);
        g2d.drawString(text, 10, 20);
        g2d.setComposite(AlphaComposite.getInstance(
                         AlphaComposite.SRC_OVER, 0.9f));
        g2d.drawString(text, 10, 50);
    }

    public void test2(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setColor(Color.black);
        g2d.drawString(text, 10, 20);
        g2d.setPaint(new GradientPaint(
                     0f, 0f, Color.BLACK, 100f, 100f, Color.GRAY));
        g2d.drawString(text, 10, 50);
    }

    public void test3(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setColor(Color.black);
        g2d.drawString(text, 10, 20);
        Shape s = new RoundRectangle2D.Double(0, 30, 400, 50, 5, 5);
        g2d.clip(s);
        g2d.drawString(text, 10, 50);
    }

    public Dimension getPreferredSize() {
        return new Dimension(500,300);
    }

    public static void disposeUI() {
        countDownLatch.countDown();
        testFrame.dispose();
    }

    public static void createTestUI() {
        testFrame.add(new LCDTextAndGraphicsState(), BorderLayout.NORTH);
        Panel resultButtonPanel = new Panel(new GridLayout(1, 2));
        Button passButton = new Button("Pass");
        passButton.addActionListener((ActionEvent e) -> {
            testResult = true;
            disposeUI();
        });
        Button failButton = new Button("Fail");
        failButton.addActionListener(e -> {
            testResult = false;
            disposeUI();
        });
        resultButtonPanel.add(passButton);
        resultButtonPanel.add(failButton);

        Panel controlUI = new Panel(new BorderLayout());
        TextArea instructions = new TextArea(
                "Instructions:\n" +
                "If you see the text six times above, press Pass.\n" +
                "If not, press Fail.",
                3,
                50,
                TextArea.SCROLLBARS_NONE
        );
        instructions.setEditable(false);
        controlUI.add(instructions, BorderLayout.CENTER);
        controlUI.add(resultButtonPanel, BorderLayout.SOUTH);

        testFrame.add(controlUI, BorderLayout.SOUTH);
        testFrame.pack();
        testFrame.setLocationRelativeTo(null);
        testFrame.setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException {
        countDownLatch = new CountDownLatch(1);
        createTestUI();
        if (!countDownLatch.await(10, TimeUnit.MINUTES)) {
            throw new RuntimeException("Timeout : No action was performed on the test UI.");
        }
        if (!testResult) {
            throw new RuntimeException("Test failed!");
        }
    }
}
