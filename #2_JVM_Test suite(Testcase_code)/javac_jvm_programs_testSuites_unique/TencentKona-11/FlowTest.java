




import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;

public class FlowTest extends Applet {
    Panel       panel;

    public FlowTest() {
        setLayout(new BorderLayout());

        
        panel = new Panel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new Button("one"));
        panel.add(new Button("two"));
        panel.add(new Button("three"));
        panel.add(new Button("four"));
        panel.add(new Button("five"));
        panel.add(new Button("six"));
        panel.add(new Button("seven"));
        panel.add(new Button("eight"));
        panel.add(new Button("nine"));
        panel.add(new Button("ten"));
        panel.add(new Button("eleven"));

        add("Center", panel);

        Panel controls = new Panel();
        controls.setLayout(new GridLayout(0, 2));

        
        {
            Choice c = new Choice();
            c.addItem("LEFT");
            c.addItem("CENTER");
            c.addItem("RIGHT");
            c.addItem("LEADING");
            c.addItem("TRAILING");
            c.addItemListener( new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    String item = (String)(e.getItem());
                    FlowLayout layout = (FlowLayout) panel.getLayout();

                    if (item.equals("LEFT")) {
                        layout.setAlignment(FlowLayout.LEFT);
                    } else if (item.equals("CENTER")) {
                        layout.setAlignment(FlowLayout.CENTER);
                    } else if (item.equals("RIGHT")) {
                        layout.setAlignment(FlowLayout.RIGHT);
                    } else if (item.equals("LEADING")) {
                        layout.setAlignment(FlowLayout.LEADING);
                    } else if (item.equals("TRAILING")) {
                        layout.setAlignment(FlowLayout.TRAILING);
                    }
                    panel.layout();
                    panel.repaint();
                }
            } );
            controls.add(new Label("FlowLayout Alignment:"));
            controls.add(c);
        }

        
        {
            Choice c = new Choice();
            c.addItem("LEFT_TO_RIGHT");
            c.addItem("RIGHT_TO_LEFT");
            c.addItem("UNKNOWN");
            c.addItemListener( new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    String item = (String)(e.getItem());

                    if (item.equals("LEFT_TO_RIGHT")) {
                        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                    } else if (item.equals("RIGHT_TO_LEFT")) {
                        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    } else {
                        panel.setComponentOrientation(ComponentOrientation.UNKNOWN);
                    }
                    panel.layout();
                    panel.repaint();
                }
            } );

            controls.add(new Label("ComponentOrientation:"));
            controls.add(c);
        }

        add("South", controls);

    }

    public static void main(String args[]) {
        Frame f = new Frame("FlowTest");

        f.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                e.getWindow().hide();
                e.getWindow().dispose();
                System.exit(0);
            };
        } );

        FlowTest flowTest = new FlowTest();
        flowTest.init();
        flowTest.start();

        f.add("Center", flowTest);
        f.setSize(300, 300);
        f.show();
    }
}
