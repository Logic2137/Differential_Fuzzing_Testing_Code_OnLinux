

package org.pear;

import java.io.Reader;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class PearScript implements ScriptEngine {

    @Override
    public Object eval(String script, ScriptContext context) {
        throw new RuntimeException();
    }

    @Override
    public Object eval(Reader reader , ScriptContext context) {
        throw new RuntimeException();
    }

    @Override
    public Object eval(String script) {
        throw new RuntimeException();
    }

    @Override
    public Object eval(Reader reader) {
        throw new RuntimeException();
    }

    @Override
    public Object eval(String script, Bindings n) {
        throw new RuntimeException();
    }

    @Override
    public Object eval(Reader reader , Bindings n) {
        throw new RuntimeException();
    }
    @Override
    public void put(String key, Object value) {
        throw new RuntimeException();
    }

    @Override
    public Object get(String key) {
        throw new RuntimeException();
    }

    @Override
    public Bindings getBindings(int scope) {
        throw new RuntimeException();
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        throw new RuntimeException();
    }

    @Override
    public Bindings createBindings() {
        throw new RuntimeException();
    }

    @Override
    public ScriptContext getContext() {
        throw new RuntimeException();
    }

    @Override
    public void setContext(ScriptContext context) {
        throw new RuntimeException();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        throw new RuntimeException();
    }
}
