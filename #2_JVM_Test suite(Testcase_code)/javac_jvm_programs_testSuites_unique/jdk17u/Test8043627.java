import javax.swing.JButton;
import javax.swing.plaf.synth.SynthButtonUI;

public class Test8043627 {

    public static void main(String[] args) {
        System.setSecurityManager(new SecurityManager());
        new SynthButtonUI().getContext(new JButton());
    }
}
