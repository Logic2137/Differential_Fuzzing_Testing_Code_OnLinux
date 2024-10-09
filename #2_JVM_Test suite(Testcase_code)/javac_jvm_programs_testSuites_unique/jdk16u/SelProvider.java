



import java.nio.channels.spi.*;

public class SelProvider {
    public static void main(String[] args) throws Exception {
        String expected = System.getProperty("java.nio.channels.spi.SelectorProvider");
        if (expected == null) {
            String osname = System.getProperty("os.name");
            String osver = System.getProperty("os.version");
            if ("Linux".equals(osname)) {
                expected = "sun.nio.ch.EPollSelectorProvider";
            } else if (osname.contains("OS X")) {
                expected = "sun.nio.ch.KQueueSelectorProvider";
            } else {
                return;
            }
        }
        String cn = SelectorProvider.provider().getClass().getName();
        System.out.println(cn);
        if (!cn.equals(expected))
            throw new Exception("failed");
    }
}
