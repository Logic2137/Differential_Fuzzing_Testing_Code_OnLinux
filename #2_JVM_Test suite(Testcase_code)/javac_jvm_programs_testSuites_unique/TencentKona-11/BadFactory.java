

import javax.script.*;
import java.util.*;


public class BadFactory implements ScriptEngineFactory {
    public String getEngineName() {
        return null;
    }

    public String getEngineVersion() {
        return null;
    }

    public List<String> getExtensions() {
        return null;
    }

    public String getLanguageName() {
        return null;
    }

    public String getLanguageVersion() {
        return null;
    }

    public String getMethodCallSyntax(String obj, String m, String[] args) {
        return null;
    }

    public List<String> getMimeTypes() {
        List<String> list = new ArrayList<String>();
        list.add("application/bad");
        list.add(null);
        list.add("");
        return list;
    }

    public List<String> getNames() {
        throw new IllegalArgumentException();
    }

    public String getOutputStatement(String str) {
        return "bad-factory-output";
    }

    public String getParameter(String key) {
        return null;
    }

    public String getProgram(String[] statements) {
        return null;
    }

    public ScriptEngine getScriptEngine() {
        return null;
    }
}
