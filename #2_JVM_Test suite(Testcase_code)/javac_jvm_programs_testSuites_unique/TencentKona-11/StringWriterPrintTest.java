



import javax.script.*;
import java.io.*;

public class StringWriterPrintTest {
    public static void main(String[] args) throws ScriptException {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("nashorn");
        if (engine == null) {
            System.out.println("Warning: No nashorn engine found; test vacuously passes.");
            return;
        }
        StringWriter sw = new StringWriter();
        engine.eval("print(\"hello world 1\\n\")");
        engine.getContext().setWriter(sw);
        
        engine.eval("print(\"hello world 2\\n\")");
        System.out.println(sw.toString());
    }
};
