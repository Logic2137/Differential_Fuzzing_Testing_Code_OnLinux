

import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import static javax.swing.SwingUtilities.invokeAndWait;



public class Test7195179 {
    public static void main(String[] args) throws Exception {
        invokeAndWait(new Runnable() {
            @Override
            public void run() {
                Integer[] items = {null, 1, 2, 3};
                JComboBox<Integer> combo = new JComboBox<>(items);
                JLabel label = new JLabel("choose:");
                JPanel panel = new JPanel();
                GroupLayout layout = new GroupLayout(panel);
                panel.setLayout(layout);
                label.setLabelFor(combo);
                combo.setSelectedIndex(0);
                combo.setRenderer(new ListCellRenderer<Integer>() {
                    private final BasicComboBoxRenderer renderer = new BasicComboBoxRenderer();

                    @Override
                    public Component getListCellRendererComponent(JList<? extends Integer> list, Integer value, int index, boolean isSelected, boolean cellHasFocus) {
                        return this.renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    }
                });
                layout.setAutoCreateContainerGaps(true);
                layout.setAutoCreateGaps(true);
                layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup().addComponent(label))
                        .addGroup(layout.createParallelGroup().addComponent(combo)));
                layout.setVerticalGroup(layout
                        .createSequentialGroup()
                        .addGroup(layout
                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label)
                                .addComponent(combo)));

                JFrame frame = new JFrame(getClass().getSimpleName());
                frame.add(panel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
