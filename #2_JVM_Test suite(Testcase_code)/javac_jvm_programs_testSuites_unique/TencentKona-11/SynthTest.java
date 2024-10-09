



import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.*;
import java.io.InputStream;
import java.awt.*;

public class SynthTest {

    public static void main(String[] args) throws Exception {
        SynthLookAndFeel laf = new SynthLookAndFeel();
        InputStream in = SynthTest.class.getResourceAsStream(
                "synthconfig.xml");
        laf.load(in, SynthTest.class);

        UIManager.setLookAndFeel(laf);

        if (!Color.RED.equals(new JButton().getForeground())) {
            throw new RuntimeException("The wrong foreground color!");
        }
    }
}
