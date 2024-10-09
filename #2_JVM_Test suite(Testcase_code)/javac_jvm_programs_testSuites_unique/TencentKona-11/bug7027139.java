



import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class bug7027139 {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                JTable orderTable = new JTable(new String[][]{
                        {"Item 1 1", "Item 1 2"},
                        {"Item 2 1", "Item 2 2"},
                        {"Item 3 1", "Item 3 2"},
                        {"Item 4 1", "Item 4 2"},
                },
                        new String[]{"Col 1", "Col 2"});

                ListSelectionModel selectionModel = orderTable.getSelectionModel();
                selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                selectionModel.addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting()) {
                            return;
                        }

                        if (e.getFirstIndex() < 0) {
                            throw new RuntimeException("Test bug7027139 failed");
                        }
                    }
                });

                orderTable.selectAll();
            }
        });

        System.out.println("Test bug7027139 passed");
    }
}
