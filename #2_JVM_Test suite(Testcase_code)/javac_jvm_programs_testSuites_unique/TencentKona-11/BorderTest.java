




import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;

public class BorderTest extends Applet {
    Panel       panel1;
    Panel       panel2;

    public BorderTest() {
        setLayout(new GridLayout(0,2));

        
        panel1 = new Panel();
        panel1.setLayout(new BorderLayout());
        panel1.add("North", new Button("North"));
        panel1.add("South", new Button("South"));
        panel1.add("East", new Button("East"));
        panel1.add("West", new Button("West"));
        panel1.add("Center", new Button("Center"));
        add(panel1);

        
        panel2 = new Panel();
        panel2.setLayout(new BorderLayout());
        panel2.add(BorderLayout.BEFORE_FIRST_LINE, new Button("FirstLine"));
        panel2.add(BorderLayout.AFTER_LAST_LINE, new Button("LastLine"));
        panel2.add(BorderLayout.BEFORE_LINE_BEGINS, new Button("FirstItem"));
        panel2.add(BorderLayout.AFTER_LINE_ENDS, new Button("LastItem"));
        panel2.add("Center", new Button("Center"));
        add(panel2);

        
        {
            Choice c = new Choice();
            c.addItem("LEFT_TO_RIGHT");
            c.addItem("RIGHT_TO_LEFT");
            c.addItem("UNKNOWN");
            c.addItemListener( new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    String item = (String)(e.getItem());

                    ComponentOrientation o = ComponentOrientation.UNKNOWN;
                    if (item.equals("LEFT_TO_RIGHT")) {
                        o = ComponentOrientation.LEFT_TO_RIGHT;
                    } else if (item.equals("RIGHT_TO_LEFT")) {
                        o = ComponentOrientation.RIGHT_TO_LEFT;
                    }
                    panel1.setComponentOrientation(o);
                    panel2.setComponentOrientation(o);
                    panel1.layout();
                    panel2.layout();
                    panel1.repaint();
                    panel2.repaint();
                }
            } );
            add(c);
        }
    }

    public static void main(String args[]) {
        Frame f = new Frame("BorderTest");

        f.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                e.getWindow().hide();
                e.getWindow().dispose();
                System.exit(0);
            };
        } );

        BorderTest BorderTest = new BorderTest();
        BorderTest.init();
        BorderTest.start();

        f.add("Center", BorderTest);
        f.setSize(450, 300);
        f.show();
    }
}
