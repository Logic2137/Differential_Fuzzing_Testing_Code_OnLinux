import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.sound.sampled.AudioFormat;

public class Properties {

    static boolean g_failed = false;

    static boolean compare(Map p1, Map p2) {
        boolean failed = false;
        for (String key : (Set<String>) p1.keySet()) {
            out("  testing key: " + key);
            if (!p2.containsKey(key)) {
                out("  missing property: '" + key + "'. Failed");
                failed = true;
            }
            Object v1 = p1.get(key);
            Object v2 = p2.get(key);
            if (((v1 == null) && (v2 != null)) || ((v1 != null) && (v2 == null)) || !(v1.equals(v2))) {
                out("  property '" + key + "' is different: " + "expected='" + v1 + "'  " + "actual='" + v2 + "'. Failed");
                failed = true;
            }
        }
        try {
            int oldSize = p2.size();
            p2.clear();
            if (oldSize > 0 && p2.size() == 0) {
                out("  could clear the properties! Failed.");
                failed = true;
            }
        } catch (Exception e) {
        }
        return failed;
    }

    public static void main(String[] argv) throws Exception {
        Map<String, Object> p = new HashMap<String, Object>();
        p.put("bitrate", new Integer(128));
        p.put("quality", new Integer(10));
        p.put("MyProp", "test");
        out("Testing AudioFileFormat properties:");
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0f, 16, 2, 4, 44100.0f, false, p);
        boolean failed = compare(p, format.properties());
        Object o = format.getProperty("MyProp");
        if (o == null || !o.equals("test")) {
            out("  getProperty did not report an existing property!");
            failed = true;
        }
        o = format.getProperty("does not exist");
        if (o != null) {
            out("  getProperty returned something for a non-existing property!");
            failed = true;
        }
        if (!failed) {
            out("  OK");
        } else {
            g_failed = true;
        }
        if (g_failed)
            throw new Exception("Test FAILED!");
        System.out.println("Test passed.");
    }

    static void out(String s) {
        System.out.println(s);
    }
}
