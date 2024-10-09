
package jdk.jfr.startupargs;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;

public class TestEventSettings {

    public static void main(String... args) throws Exception {
        String subTest = args[0];
        System.out.println(subTest);
        switch(subTest) {
            case "knownSetting":
                assertSetting("jdk.JVMInformation#enabled", "false");
            case "unknownSetting":
                assertSetting("com.example.Hello#stackTrace", null);
            case "addedUnknownSetting":
                assertSetting("HelloWorld#enabled", "true");
            case "multipleSettings":
                {
                    assertSetting("A.B#enabled", "true");
                    assertSetting("C.D#enabled", "false");
                }
            case "jfcOptions":
                {
                    assertSetting("jdk.ClassDefine#enabled", "true");
                    assertSetting("jdk.SocketRead#threshold", "100 ms");
                }
            default:
                throw new Exception("Uknown tes " + subTest);
        }
    }

    private static void assertSetting(String key, String value) throws Exception {
        List<Recording> rs = FlightRecorder.getFlightRecorder().getRecordings();
        if (rs.isEmpty()) {
            throw new Exception("No recording started");
        }
        if (rs.size() != 1) {
            throw new Exception("Expected only one recording");
        }
        Map<String, String> currentSettings = rs.get(0).getSettings();
        String s = currentSettings.get(key);
        if (!Objects.equals(s, value)) {
            System.out.println("Key:" + key);
            System.out.println("Value:" + value);
            System.out.println("Result: " + s);
            System.out.println("All Setting:");
            for (var entry : currentSettings.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
            throw new Exception("Expected: " + value + " for " + key + " , got: " + s);
        }
    }
}
