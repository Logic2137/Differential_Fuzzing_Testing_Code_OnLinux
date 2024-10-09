import java.io.*;
import javax.swing.*;
import javax.swing.plaf.metal.*;

public class bug7193219 {

    private static byte[] serializeGUI() {
        JFrame frame = new JFrame("Serialization");
        JPanel mainPanel = new JPanel();
        JComboBox status = new JComboBox();
        status.addItem("123");
        mainPanel.add(status);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(mainPanel);
            oos.flush();
            frame.dispose();
            return baos.toByteArray();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private static void deserializeGUI(byte[] serializedData) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedData));
            JPanel mainPanel = (JPanel) ois.readObject();
            JFrame frame = new JFrame("Deserialization");
            frame.getContentPane().add(mainPanel);
            frame.pack();
            frame.dispose();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new MetalLookAndFeel());
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                deserializeGUI(serializeGUI());
            }
        });
    }
}
