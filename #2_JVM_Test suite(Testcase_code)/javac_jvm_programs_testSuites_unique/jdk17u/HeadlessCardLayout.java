import java.awt.*;

public class HeadlessCardLayout {

    public static void main(String[] args) {
        CardLayout cl;
        cl = new CardLayout();
        cl = new CardLayout(10, 10);
        cl.getHgap();
        cl.setHgap(10);
        cl.getVgap();
        cl.setVgap(10);
    }
}
