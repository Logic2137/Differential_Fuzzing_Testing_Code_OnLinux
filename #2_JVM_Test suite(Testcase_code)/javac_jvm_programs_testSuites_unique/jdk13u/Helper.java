import javax.script.*;

public class Helper {

    private Helper() {
    }

    public static ScriptEngine getJsEngine(ScriptEngineManager m) {
        ScriptEngine e = m.getEngineByName("nashorn");
        if (e == null && System.getProperty("java.runtime.name").startsWith("Java(TM)")) {
            throw new RuntimeException("no js engine found");
        }
        return e;
    }
}
