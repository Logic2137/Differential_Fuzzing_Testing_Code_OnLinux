import javax.script.*;
import java.util.function.*;

public class TestMethodNames {

    public static void main(String[] args) throws Exception {
        ScriptEngineManager m = new ScriptEngineManager();
        ScriptEngine e = m.getEngineByName("nashorn");
        e.eval("({get \"\0\"(){}})[\"\0\"]");
        e.eval("({get \"\\x80\"(){}})[\"\\x80\"]");
        e.eval("({get \"\\xff\"(){}})[\"\\xff\"]");
    }
}
