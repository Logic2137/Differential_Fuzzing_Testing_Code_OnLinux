

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Variable {
    private static Map<String,Variable> vars = new HashMap<String,Variable>();

    private String name;
    private long value;

    Variable(String name, long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int intValue() {
        if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
            throw new RuntimeException("Overflow/Underflow: " + value);
        }
        return (int) value;
    }

    public long longValue() {
        return value;
    }

    public void setValue(int v) { value = v; }
    public void setValue(long v) { value = v; }

    

    public static Variable newVar(String name, long value) {
        if (name.charAt(0) != '$') {
            throw new RuntimeException("wrong var name: " + name);
        }
        String s = name.toLowerCase(Locale.ROOT);
        Variable v = new Variable(name, value);
        put(name, v);
        return v;
    }

    static void put(String s, Variable var) {
        vars.put(s, var);
    }

    public static Variable get(String name) {
        return vars.get(name);
    }
}
