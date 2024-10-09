import java.awt.Toolkit;

public final class GetMulticlickTime {

    public static void main(final String[] args) {
        Integer time = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
        if (time == null || time <= 0 || time > 30_000) {
            throw new RuntimeException("awt.multiClickInterval:" + time);
        }
    }
}
