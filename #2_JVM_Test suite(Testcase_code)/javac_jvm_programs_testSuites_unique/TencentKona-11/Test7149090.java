



import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Test7149090 {
    private static final Object[][] DEFAULT_TITLE_POSITIONS = {
            {"Metal", TitledBorder.TOP},
            {"Motif", TitledBorder.TOP},
            {"Windows", TitledBorder.TOP},
            {"Nimbus", TitledBorder.ABOVE_TOP},
    };

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo lookAndFeel : UIManager.getInstalledLookAndFeels()) {
            for (Object[] defaultTitlePosition : DEFAULT_TITLE_POSITIONS) {
                if (defaultTitlePosition[0].equals(lookAndFeel.getName())) {
                    UIManager.setLookAndFeel(lookAndFeel.getClassName());

                    final int expectedPosition = (Integer) defaultTitlePosition[1];

                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            List<TitledBorder> borders = new ArrayList<>();

                            borders.add(BorderFactory.createTitledBorder(new EmptyBorder(0, 0, 0, 0), "Title"));

                            try {
                                Method getPositionMethod = TitledBorder.class.getDeclaredMethod("getPosition");

                                getPositionMethod.setAccessible(true);

                                for (TitledBorder border : borders) {
                                    int position = (Integer) getPositionMethod.invoke(border);

                                    if (position != expectedPosition) {
                                        throw new RuntimeException("Invalid title position");
                                    }
                                }
                            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                    System.out.println("Test passed for LookAndFeel " + lookAndFeel.getName());
                }
            }
        }
    }
}
