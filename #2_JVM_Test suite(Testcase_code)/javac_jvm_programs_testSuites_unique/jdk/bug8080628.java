

import java.util.Locale;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import sun.swing.SwingUtilities2;


public class bug8080628 {
    public static final String[] MNEMONIC_KEYS = new String[] {
            "FileChooser.saveButtonMnemonic",
            "FileChooser.openButtonMnemonic",
            "FileChooser.cancelButtonMnemonic",
            "FileChooser.directoryOpenButtonMnemonic"
    };

    public static final Locale[] LOCALES = new Locale[] {
            new Locale("en"),
            new Locale("de"),
            new Locale("es"),
            new Locale("fr"),
            new Locale("it"),
            new Locale("ja"),
            new Locale("ko"),
            new Locale("pt", "BR"),
            new Locale("sv"),
            new Locale("zh", "CN"),
            new Locale("zh", "TW")
    };

    private static volatile Exception exception;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                runTest();
            }
        });

        if (exception != null) {
            throw exception;
        }
    }

    private static void runTest() {
        try {
            LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
            for (LookAndFeelInfo info : lafInfo) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (final UnsupportedLookAndFeelException ignored) {
                    continue;
                }

                for (Locale locale : LOCALES) {
                    for (String key : MNEMONIC_KEYS) {
                        int mnemonic = SwingUtilities2.getUIDefaultsInt(key, locale);
                        if (mnemonic != 0) {
                            throw new RuntimeException("No mnemonic expected (" + mnemonic + ") " +
                                    "for '" + key + "' " +
                                    "in locale '" + locale + "' " +
                                    "in Look-and-Feel '"
                                        + UIManager.getLookAndFeel().getClass().getName() + "'");
                        }
                    }
                }
            }
            System.out.println("Test passed");
        } catch (Exception e) {
            exception = e;
        }
    }

}
