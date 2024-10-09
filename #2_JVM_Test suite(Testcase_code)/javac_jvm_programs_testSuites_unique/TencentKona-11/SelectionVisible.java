


import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.TextArea;

public final class SelectionVisible extends Applet {

    private TextArea ta;

    @Override
    public void init() {
        ta = new TextArea(4, 20);
        ta.setText("01234\n56789");
        ta.select(3, 9);

        final TextArea instruction = new TextArea("INSTRUCTIONS:\n"
                                                 + "The text 34567 should be selected in the TextArea.\n"
                                                 + "If this is what you observe, then the test passes.\n"
                                                 + "Otherwise, the test fails.", 40, 5,
                                         TextArea.SCROLLBARS_NONE);
        instruction.setEditable(false);
        instruction.setPreferredSize(new Dimension(300, 70));
        final Panel panel = new Panel();
        panel.setLayout(new FlowLayout());
        panel.add(ta);
        setLayout(new BorderLayout());
        add(instruction, BorderLayout.CENTER);
        add(panel, BorderLayout.PAGE_END);
    }

    @Override
    public void start() {
        setVisible(true);
        ta.requestFocus();
    }
}
