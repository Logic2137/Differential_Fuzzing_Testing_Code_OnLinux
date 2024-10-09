import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class AccessibleJListChildNPETest {

    private static String[] model = { "1", "2", "3", "4", "5", "6" };

    private static JList<String> list;

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                final MyModel dataModel = new MyModel(Arrays.asList(model));
                list = new JList<>(dataModel);
                frame.getContentPane().add(list);
                frame.pack();
                frame.setVisible(true);
            }
        });
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                AccessibleContext ac = list.getAccessibleContext();
                MyModel model = (MyModel) list.getModel();
                Accessible accessibleChild = ac.getAccessibleChild(model.getSize() - 1);
                model.removeFirst();
                accessibleChild.getAccessibleContext().getAccessibleSelection();
                accessibleChild.getAccessibleContext().getAccessibleText();
                accessibleChild.getAccessibleContext().getAccessibleValue();
            }
        });
    }

    protected static class MyModel extends AbstractListModel<String> {

        private List<String> items = new ArrayList<>();

        MyModel(final List<String> newItems) {
            super();
            items.addAll(newItems);
            fireIntervalAdded(this, 0, getSize() - 1);
        }

        void removeFirst() {
            if (getSize() > 0) {
                items.remove(0);
                fireIntervalRemoved(this, 0, 0);
            }
        }

        @Override
        public int getSize() {
            return items.size();
        }

        @Override
        public String getElementAt(int index) {
            return items.get(index);
        }
    }
}
