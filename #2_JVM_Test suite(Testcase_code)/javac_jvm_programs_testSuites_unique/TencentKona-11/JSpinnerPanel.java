
package com.sun.swingset3.demos.spinner;

import javax.swing.*;
import java.awt.*;



public class JSpinnerPanel extends JPanel {

    private final JPanel labelPanel;
    private final JPanel spinnerPanel;

    public JSpinnerPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(0, 1));

        spinnerPanel = new JPanel();
        spinnerPanel.setLayout(new GridLayout(0, 1));

        add(labelPanel);
        add(Box.createHorizontalStrut(5));
        add(spinnerPanel);
    }

    public void addSpinner(String labelText, JSpinner spinner) {
        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(SwingConstants.TRAILING);
        labelPanel.add(label);

        JPanel flowPanel = new JPanel();
        flowPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 1));
        flowPanel.add(spinner);
        spinnerPanel.add(flowPanel);
    }
}


