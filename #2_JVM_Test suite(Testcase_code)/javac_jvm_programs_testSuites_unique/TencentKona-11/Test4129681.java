



import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class Test4129681 extends JApplet implements ItemListener {
    private JLabel label;

    @Override
    public void init() {
        JCheckBox check = new JCheckBox("disable");
        check.addItemListener(this);

        this.label = new JLabel("message");
        this.label.setBorder(BorderFactory.createTitledBorder("label"));
        this.label.setEnabled(!check.isSelected());

        add(BorderLayout.NORTH, check);
        add(BorderLayout.CENTER, this.label);
    }

    public void itemStateChanged(ItemEvent event) {
        this.label.setEnabled(ItemEvent.DESELECTED == event.getStateChange());
    }
}
