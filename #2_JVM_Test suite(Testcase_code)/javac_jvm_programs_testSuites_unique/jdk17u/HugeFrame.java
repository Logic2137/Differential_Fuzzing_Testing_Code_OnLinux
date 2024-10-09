import java.awt.*;

public class HugeFrame {

    public static void main(String[] args) throws Exception {
        Frame f = new Frame("Huge");
        f.setBounds(10, 10, 30000, 500000);
        f.setVisible(true);
        Thread.sleep(1000);
        System.err.println(f.getBounds());
        f.dispose();
    }
}
