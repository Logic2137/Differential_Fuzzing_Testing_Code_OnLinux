

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Robot;


public final class SetStateExcessEvent {

    private static boolean failed;

    public static void main(final String[] args) throws Exception {
        final Robot robot = new Robot();
        final CheckboxGroup group = new CheckboxGroup();
        final Checkbox[] cbs = {new Checkbox("checkbox1", true, group),
                                new Checkbox("checkbox2", false, group),
                                new Checkbox("checkbox3", true, group),

                                new Checkbox("checkbox4", true),
                                new Checkbox("checkbox5", false),
                                new Checkbox("checkbox6", true)};
        final Frame frame = new Frame();
        frame.setLayout(new GridBagLayout());
        try {
            for (final Checkbox cb : cbs) {
                cb.addItemListener(e -> {
                    failed = true;
                });
            }
            for (final Checkbox cb : cbs) {
                frame.add(cb);
            }
            frame.pack();

            for (final Checkbox cb : cbs) {
                cb.setState(!cb.getState());
            }

            for (final Checkbox cb : cbs) {
                group.setSelectedCheckbox(cb);
            }
            robot.waitForIdle();
        } finally {
            frame.dispose();
        }
        if (failed) {
            throw new RuntimeException("Listener should not be called");
        }
        System.out.println("Test passed");
    }
}
