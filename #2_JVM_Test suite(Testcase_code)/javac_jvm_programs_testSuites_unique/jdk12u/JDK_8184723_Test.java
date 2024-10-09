

package jdk.nashorn.internal.runtime.linker.test;

import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;



public class JDK_8184723_Test {
    public static void main(String args[]) throws Exception {
        NashornScriptEngine engine = (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine();
        AbstractJSObject obj = new AbstractJSObject() {
            @Override
            public Object getMember(String name) {
                return this;
            }

            @Override
            public Object call(Object thiz, Object... args) {
                return thiz;
            }

        };

        engine.put("a", obj);
        engine.eval("function b(){ a.apply(null,arguments);};b();");
    }
}
