


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;

public class JTabbedPaneSerialization {

    public static void main(final String[] args) throws Exception {

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Tab1", new JLabel("Label1"));
        tabbedPane.addTab("Tab2", new JLabel("Label2"));
        tabbedPane.addTab("Tab3", new JLabel("Label3"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);

        out.writeObject(tabbedPane);
        out.close();

        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ObjectInputStream oin = new ObjectInputStream(is);

        final JTabbedPane readPane = (JTabbedPane) oin.readObject();
        System.out.println("readPane: " + readPane.toString());
        oin.close();
        if (tabbedPane.getTabCount() != readPane.getTabCount()) {
            System.out.println("tabbedPane.tabCount " +
                                          tabbedPane.getTabCount());
            System.out.println("readPane.tabCount " +
                                            readPane.getTabCount());
            throw new
               RuntimeException("Number of pages/tab of JTabbedPane is not deserialized");
        }
    }
}
