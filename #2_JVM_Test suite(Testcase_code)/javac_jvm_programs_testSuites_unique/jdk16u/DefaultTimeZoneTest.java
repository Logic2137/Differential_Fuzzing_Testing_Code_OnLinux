



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

public class DefaultTimeZoneTest extends JApplet implements Runnable {
    static final String FORMAT = "yyyy-MM-dd HH:mm:ss zzzz (XXX)";
    JLabel tzid;
    JLabel label;
    SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
    JButton button = new JButton("English");
    Thread clock;
    boolean english = false;

    @Override
    public void init() {
        tzid = new JLabel("Time zone ID: " + sdf.getTimeZone().getID(), SwingConstants.CENTER);
        tzid.setAlignmentX(Component.CENTER_ALIGNMENT);
        label = new JLabel(sdf.format(new Date()), SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {
                @Override
                @SuppressWarnings("deprecation")
                public void actionPerformed(ActionEvent e) {
                    english = (english == false);
                    Locale loc = english ? Locale.US : Locale.getDefault();
                    sdf = new SimpleDateFormat(FORMAT, loc);
                    button.setLabel(!english ? "English" : "Local");
                }
            });
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(tzid);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(button);
        getContentPane().add(panel);
    }

    @Override
    public void start() {
        clock = new Thread(this);
        clock.start();
    }

    @Override
    public void stop() {
        clock = null;
    }

    @Override
    public void run() {
        Thread me = Thread.currentThread();

        while (clock == me) {
            
            
            TimeZone.setDefault(null);
            System.setProperty("user.timezone", "");
            TimeZone tz = TimeZone.getDefault();
            sdf.setTimeZone(tz);
            tzid.setText("Time zone ID: " + tz.getID());
            label.setText(sdf.format(new Date()));
            repaint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
