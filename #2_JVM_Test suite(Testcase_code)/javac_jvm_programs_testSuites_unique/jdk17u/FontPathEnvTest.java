import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FontPathEnvTest {

    public static void main(String[] args) {
        String env = System.getenv("JAVA2D_FONTPATH");
        if (env == null) {
            createChild();
        } else {
            String prop = System.getProperty("sun.java2d.fontpath");
            if (prop != null && env.equals(prop)) {
                throw new RuntimeException("sun.java2d.fontpath property set");
            }
        }
    }

    static void createChild() {
        String cpDir = System.getProperty("java.class.path");
        Map<String, String> env = new HashMap<String, String>();
        env.put("JAVA2D_FONTPATH", "anyValue");
        String jHome = System.getProperty("java.home");
        String jCmd = jHome + File.separator + "bin" + File.separator + "java";
        int exitValue = doExec(env, jCmd, "-cp", cpDir, "FontPathEnvTest");
        if (exitValue != 0) {
            throw new RuntimeException("Test Failed");
        }
    }

    static int doExec(Map<String, String> envToSet, String... cmds) {
        Process p = null;
        ProcessBuilder pb = new ProcessBuilder(cmds);
        Map<String, String> env = pb.environment();
        for (String cmd : cmds) {
            System.out.print(cmd + " ");
        }
        System.out.println();
        if (envToSet != null) {
            env.putAll(envToSet);
        }
        BufferedReader rdr = null;
        try {
            pb.redirectErrorStream(true);
            p = pb.start();
            rdr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String in = rdr.readLine();
            while (in != null) {
                in = rdr.readLine();
                System.out.println(in);
            }
            p.waitFor();
            p.destroy();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p.exitValue();
    }
}
