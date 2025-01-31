

import javax.script.*;


public class CheckEngine {
    public static void main(String... args) {
        int exitCode = 0;
        ScriptEngine engine =
            (new ScriptEngineManager()).getEngineByName("nashorn");

        if (engine == null &&
            !(System.getProperty("java.runtime.name").startsWith("Java(TM)"))) {
            exitCode = 2;
        }

        System.exit(exitCode);
    }
}
