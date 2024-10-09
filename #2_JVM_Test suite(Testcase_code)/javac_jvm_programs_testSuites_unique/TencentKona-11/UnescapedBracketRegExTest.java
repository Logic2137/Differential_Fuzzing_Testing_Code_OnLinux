



import javax.script.*;
import java.io.*;

public class UnescapedBracketRegExTest {
    public static void main(String[] args) throws ScriptException {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("nashorn");
        if (engine == null) {
            System.out.println("Warning: No nashorn engine found; test vacuously passes.");
            return;
        }
        
        engine.eval("var x = /[a-zA-Z+/=]/;");
    }
};
