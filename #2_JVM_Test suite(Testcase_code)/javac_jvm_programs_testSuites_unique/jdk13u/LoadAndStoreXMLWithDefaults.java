import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadAndStoreXMLWithDefaults {

    static String writeToXML(Properties props) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        props.storeToXML(baos, "Test 8016344");
        return baos.toString();
    }

    static Properties loadFromXML(String xml, Properties defaults) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Properties props = new Properties(defaults);
        props.loadFromXML(bais);
        return props;
    }

    static enum Objects {

        OBJ1, OBJ2, OBJ3
    }

    public static void main(String[] args) throws IOException {
        Properties p1 = new Properties();
        p1.setProperty("p1.prop", "prop1-p1");
        p1.setProperty("p1.and.p2.prop", "prop2-p1");
        p1.setProperty("p1.and.p2.and.p3.prop", "prop3-p1");
        Properties p2 = new Properties(p1);
        p2.setProperty("p2.prop", "prop4-p2");
        p2.setProperty("p1.and.p2.prop", "prop5-p2");
        p2.setProperty("p1.and.p2.and.p3.prop", "prop6-p2");
        p2.setProperty("p2.and.p3.prop", "prop7-p2");
        Properties p3 = new Properties(p2);
        p3.setProperty("p3.prop", "prop8-p3");
        p3.setProperty("p1.and.p2.and.p3.prop", "prop9-p3");
        p3.setProperty("p2.and.p3.prop", "prop10-p3");
        Properties P1 = loadFromXML(writeToXML(p1), null);
        Properties P2 = loadFromXML(writeToXML(p2), P1);
        Properties P3 = loadFromXML(writeToXML(p3), P2);
        testResults(p1, P1, p2, P2, p3, P3);
        P1.put("p1.object.prop", Objects.OBJ1);
        P1.put(Objects.OBJ1, "p1.object.prop");
        P1.put("p2.object.prop", "p2.object.prop");
        P2.put("p2.object.prop", Objects.OBJ2);
        P2.put(Objects.OBJ2, "p2.object.prop");
        P3.put("p3.object.prop", Objects.OBJ3);
        P3.put(Objects.OBJ3, "p3.object.prop");
        Properties PP1 = loadFromXML(writeToXML(P1), null);
        Properties PP2 = loadFromXML(writeToXML(P2), PP1);
        Properties PP3 = loadFromXML(writeToXML(P3), PP2);
        p1.setProperty("p2.object.prop", "p2.object.prop");
        try {
            testResults(p1, PP1, p2, PP2, p3, PP3);
        } finally {
            p1.remove("p2.object.prop");
        }
    }

    public static void testResults(Properties... pps) {
        for (int i = 0; i < pps.length; i += 2) {
            if (!pps[i].equals(pps[i + 1])) {
                System.err.println("P" + (i / 2 + 1) + " Reloaded properties differ from original");
                System.err.println("\toriginal: " + pps[i]);
                System.err.println("\treloaded: " + pps[i + 1]);
                throw new RuntimeException("P" + (i / 2 + 1) + " Reloaded properties differ from original");
            }
            if (!pps[i].keySet().equals(pps[i + 1].keySet())) {
                System.err.println("P" + (i / 2 + 1) + " Reloaded property names differ from original");
                System.err.println("\toriginal: " + pps[i].keySet());
                System.err.println("\treloaded: " + pps[i + 1].keySet());
                throw new RuntimeException("P" + (i / 2 + 1) + " Reloaded property names differ from original");
            }
            if (!pps[i].stringPropertyNames().equals(pps[i + 1].stringPropertyNames())) {
                System.err.println("P" + (i / 2 + 1) + " Reloaded string property names differ from original");
                System.err.println("\toriginal: " + pps[i].stringPropertyNames());
                System.err.println("\treloaded: " + pps[i + 1].stringPropertyNames());
                throw new RuntimeException("P" + (i / 2 + 1) + " Reloaded string property names differ from original");
            }
        }
    }
}
