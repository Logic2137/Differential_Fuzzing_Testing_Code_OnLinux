import javax.script.*;

public class Helper {

    private Helper() {
    }

    public static ScriptEngine getJsEngine(ScriptEngineManager m) {
        return m.getEngineByName("nashorn");
    }
}
