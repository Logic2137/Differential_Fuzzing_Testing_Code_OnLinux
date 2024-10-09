



import sun.awt.SunToolkit;

import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthLookAndFeel;
import java.lang.reflect.Method;

public class bug7143614 {
    private static Method setSelectedUIMethod;

    private static ComponentUI componentUI = new BasicButtonUI();

    public static void main(String[] args) throws Exception {
        setSelectedUIMethod = SynthLookAndFeel.class.getDeclaredMethod("setSelectedUI", ComponentUI.class,
                boolean.class, boolean.class, boolean.class, boolean.class);
        setSelectedUIMethod.setAccessible(true);

        setSelectedUIMethod.invoke(null, componentUI, true, true, true, true);

        validate();

        Thread thread = new ThreadInAnotherAppContext();

        thread.start();
        thread.join();

        validate();

        System.out.println("Test bug7143614 passed.");
    }

    private static void validate() throws Exception {
        Method getSelectedUIMethod = SynthLookAndFeel.class.getDeclaredMethod("getSelectedUI");

        getSelectedUIMethod.setAccessible(true);

        Method getSelectedUIStateMethod = SynthLookAndFeel.class.getDeclaredMethod("getSelectedUIState");

        getSelectedUIStateMethod.setAccessible(true);

        if (getSelectedUIMethod.invoke(null) != componentUI) {
            throw new RuntimeException("getSelectedUI returns invalid value");
        }
        if (((Integer) getSelectedUIStateMethod.invoke(null)).intValue() !=
                (SynthConstants.SELECTED | SynthConstants.FOCUSED)) {
            throw new RuntimeException("getSelectedUIState returns invalid value");
        }

    }

    private static class ThreadInAnotherAppContext extends Thread {
        public ThreadInAnotherAppContext() {
            super(new ThreadGroup("7143614"), "ThreadInAnotherAppContext");
        }

        public void run() {
            SunToolkit.createNewAppContext();

            try {
                setSelectedUIMethod.invoke(null, null, false, false, false, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
