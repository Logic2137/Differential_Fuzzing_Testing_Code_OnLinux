

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public final class DeprecatedAppletViewer {

    private static final String TEXT
            = "Warning: Applet API and AppletViewer are deprecated.";

    public static void main(final String[] args) {
        final PrintStream old = System.err;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
        final PrintStream ps = new PrintStream(baos);
        try {
            System.setErr(ps);
            sun.applet.Main.main(new String[]{});
        } finally {
            System.setErr(old);
        }

        final String text = new String(baos.toByteArray());
        if (!text.contains(TEXT)) {
            System.err.println("The text should contain: \"" + TEXT + "\"");
            System.err.println("But the current text is: ");
            System.err.println(text);
            throw new RuntimeException("Error");
        }
    }
}
