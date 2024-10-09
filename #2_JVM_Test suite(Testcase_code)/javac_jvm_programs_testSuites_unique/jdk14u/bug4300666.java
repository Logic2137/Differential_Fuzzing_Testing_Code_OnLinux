



import javax.swing.*;

public class bug4300666 {

    public static void main(final String[] args) {
        UIDefaults d = UIManager.getDefaults();
        d.toString();
    }
}
