



import sun.security.krb5.KdcComm;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DefUdpLimit {

    public static void main(String[] args) throws Exception {
        int set = Integer.valueOf(args[0]);
        int expected = Integer.valueOf(args[1]);
        Field f = KdcComm.class.getDeclaredField("defaultUdpPrefLimit");
        f.setAccessible(true);
        writeConf(set);
        int actual = (Integer)f.get(null);
        if (actual != expected) {
            throw new Exception("Expected: " + expected + ", get " + actual);
        }
    }

    static void writeConf(int i) throws Exception {
        String file = "krb5.conf." + i;
        String content = "[libdefaults]\n";
        if (i >= 0) {
            content += "udp_preference_limit = " + i;
        }
        Files.write(Paths.get(file), content.getBytes());
        System.setProperty("java.security.krb5.conf", file);
    }
}

