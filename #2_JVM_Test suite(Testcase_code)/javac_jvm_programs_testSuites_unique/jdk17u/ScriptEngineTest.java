
package jdk.test.engines;

import javax.script.*;

public class ScriptEngineTest {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100; i++) {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine scriptEngine = manager.getEngineByName("js");
            if (!(scriptEngine.toString().contains("jdk.dummyNashorn.api.scripting.DummyNashornJSEngine")))
                throw new RuntimeException("Script EngineOrder is inconsistent");
        }
    }
}
