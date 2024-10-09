import java.awt.Frame;

public final class NullFontValidate {

    public static void main(final String[] args) {
        final Frame frame = new Frame();
        try {
            frame.pack();
            frame.setFont(null);
            frame.validate();
        } finally {
            frame.dispose();
        }
    }
}
