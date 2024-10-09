import java.awt.Window;

public class SetBackgroundNPE {

    public static void main(String[] args) {
        new Window(null).setBackground(null);
    }
}
