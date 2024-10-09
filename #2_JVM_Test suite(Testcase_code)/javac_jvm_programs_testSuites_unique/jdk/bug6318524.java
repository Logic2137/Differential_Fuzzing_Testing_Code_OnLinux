

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;



public class bug6318524 {
    private static final String LONG_WORD = "consequences";
    private static final String TEXT = "Justified: "
            + LONG_WORD + " " + LONG_WORD;
    private static final int REPEAT_COUNT = 18;

    private static JTextPane textPane;
    private static Dimension bounds;

    private static int step = 0;

    private static Shape firstLineEndsAt;

    public static void main(String[] args) throws Throwable {
        List<String> argList = Arrays.asList(args);

        
        final boolean showFrame = argList.contains("-show");
        
        final boolean saveAllImages = argList.contains("-saveAll");
        
        final boolean saveImage = saveAllImages || argList.contains("-save");

        SwingUtilities.invokeAndWait(() -> {
            createUI(showFrame);
            paintToImage(step++, saveAllImages);
            makeLineJustified();
            paintToImage(step++, saveImage);

            firstLineEndsAt = getEndOfFirstLine();

            moveCursorToStart();
            pressEnter(saveAllImages);

            paintToImage(step++, saveImage);
            checkLineJustified();
        });
    }

    private static void createUI(boolean showFrame) {
        textPane = new JTextPane();
        textPane.setText(TEXT);

        FontMetrics fm = textPane.getFontMetrics(textPane.getFont());
        int textWidth = fm.stringWidth(LONG_WORD);
        int textHeight = fm.getHeight();
        bounds = new Dimension(2 * textWidth,
                               (REPEAT_COUNT + 3) * textHeight);
        textPane.setPreferredSize(bounds);
        textPane.setSize(bounds);

        if (showFrame) {
            JFrame frame = new JFrame("bug6318524");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            frame.getContentPane().add(textPane);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    private static void makeLineJustified() {
        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setAlignment(sas, StyleConstants.ALIGN_JUSTIFIED);
        textPane.setParagraphAttributes(sas, false);
    }

    private static void moveCursorToStart() {
        
        Caret caret = textPane.getCaret();
        caret.setDot(0);
    }

    private static void pressEnter(boolean saveImages) {
        Document doc = textPane.getDocument();
        try {
            for (int i = 0; i < REPEAT_COUNT; i++) {
                
                doc.insertString(0, "\n", null);
                
                paintToImage(step++, saveImages);
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkLineJustified() {
        Shape newPosition = getEndOfFirstLine();
        if (((Rectangle) firstLineEndsAt).x != ((Rectangle) newPosition).x) {
            System.err.println("Old: " + firstLineEndsAt);
            System.err.println("New: " + newPosition);
            throw new RuntimeException("The first line of the paragraph is not justified");
        }
    }

    private static Shape getEndOfFirstLine() {
        try {
            final View rootView = textPane.getUI().getRootView(textPane);
            final View boxView = rootView.getView(0);
            final View paragraphView = boxView.getView(boxView.getViewCount() - 1);
            assert paragraphView.getViewCount() == 2;
            final View rowView = paragraphView.getView(0);
            return rowView.getView(0)
                          .modelToView(rowView.getEndOffset() - 1,
                                       textPane.getBounds(),
                                       Position.Bias.Backward);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void paintToImage(final int step, boolean saveImage) {
        BufferedImage im = new BufferedImage(bounds.width, bounds.height,
                TYPE_INT_RGB);
        Graphics g = im.getGraphics();
        textPane.paint(g);
        g.dispose();
        if (saveImage) {
            saveImage(im, String.format("%02d.png", step));
        }
    }

    private static void saveImage(BufferedImage image, String fileName) {
        try {
            ImageIO.write(image, "png", new File(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
