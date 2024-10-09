import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyleFactory;
import javax.swing.plaf.synth.SynthStyle;

public class TestCustomStyleFactory {

    private static final String GTK_LAF_CLASS = "GTKLookAndFeel";

    public static void main(String[] args) throws Exception {
        if (!System.getProperty("os.name").startsWith("Linux")) {
            System.out.println("This test is meant for Linux platform only");
            return;
        }
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            if (lookAndFeelInfo.getClassName().contains(GTK_LAF_CLASS)) {
                try {
                    UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
                } catch (final UnsupportedLookAndFeelException ignored) {
                    System.out.println("GTK L&F could not be set, so this " + "test can not be run in this scenario ");
                    return;
                }
            }
        }
        final SynthStyleFactory original = SynthLookAndFeel.getStyleFactory();
        SynthLookAndFeel.setStyleFactory(new SynthStyleFactory() {

            @Override
            public SynthStyle getStyle(JComponent c, Region id) {
                return original.getStyle(c, id);
            }
        });
        new JLabel("test");
    }
}
