





import javax.swing.*;
import java.lang.reflect.Field;

public class bug7089914 {

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            System.out.println("Not WindowsLookAndFeel, test skipped");

            return;
        }

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {

                JRadioButton rb = new JRadioButton();

                if (!"com.sun.java.swing.plaf.windows.WindowsRadioButtonUI".equals(rb.getUI().getClass().getName())) {
                    throw new RuntimeException("Unexpected UI class of JRadioButton");
                }

                try {
                    Field initializedField = rb.getUI().getClass().getDeclaredField("initialized");
                    initializedField.setAccessible(true);

                    if (!initializedField.getBoolean(rb.getUI())) {
                        throw new RuntimeException("initialized is false");
                    }

                    rb.getUI().uninstallUI(rb);

                    if (initializedField.getBoolean(rb.getUI())) {
                        throw new RuntimeException("initialized is true");
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
