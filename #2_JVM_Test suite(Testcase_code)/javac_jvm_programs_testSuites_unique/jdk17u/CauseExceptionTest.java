import javax.script.*;
import java.io.*;

public class CauseExceptionTest {

    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("nashorn");
        if (engine == null) {
            System.out.println("Warning: No js engine found; test vacuously passes.");
            return;
        }
        engine.eval("function hello_world() { print('hello world'); throw 'out of here'; } ");
        Invocable invocable = (Invocable) engine;
        try {
            invocable.invokeFunction("hello_world", (Object[]) null);
        } catch (ScriptException se) {
            Throwable cause = se.getCause();
            if (cause == null) {
                throw new RuntimeException("null cause");
            }
            System.out.println(cause);
        }
    }
}
