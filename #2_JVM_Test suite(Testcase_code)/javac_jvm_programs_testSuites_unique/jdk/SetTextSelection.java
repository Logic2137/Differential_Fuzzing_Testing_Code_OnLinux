

import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.TextField;


public final class SetTextSelection {

    private static final String LONG_TEXT = "text field";
    private static final String SHORT_TEXT = "text";

    public static void main(String[] args) {
        testNoFrame(true);
        testNoFrame(false);
        for (int i = 0; i < 5; i++) {
            testFrame(true, i);
            testFrame(false, i);
        }
        testDisposedFrame(true);
        testDisposedFrame(false);
    }

    private static void testNoFrame(boolean field) {
        TextComponent tf = field ? new TextField(LONG_TEXT) :
                                   new TextArea(LONG_TEXT);
        tf.selectAll();
        tf.setText(SHORT_TEXT);
        test(tf);
    }

    private static void testDisposedFrame(boolean field) {
        Frame frame = new Frame();
        try {
            TextComponent tf = field ? new TextField(LONG_TEXT) :
                                       new TextArea(LONG_TEXT);
            frame.add(tf);
            frame.pack();
            tf.selectAll();
            frame.dispose();
            tf.setText(SHORT_TEXT);
            test(tf);
        } finally {
            frame.dispose();
        }
    }

    private static void testFrame(boolean field, int step) {
        Frame frame = new Frame();
        try {
            TextComponent tf = field ? new TextField(LONG_TEXT) :
                                       new TextArea(LONG_TEXT);
            if (step == 1) {
                frame.pack();
            }
            frame.add(tf);
            if (step == 2) {
                frame.pack();
            }
            tf.selectAll();
            if (step == 3) {
                frame.pack();
            }
            tf.setText(SHORT_TEXT);
            if (step == 4) {
                frame.pack();
            }
            test(tf);
        } finally {
            frame.dispose();
        }
    }

    private static void test(TextComponent tf) {
        String str = tf.getSelectedText();
        if (!str.equals(SHORT_TEXT)) {
            throw new RuntimeException(str);
        }
    }
}
