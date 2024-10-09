
package jdk.nashorn.test.models;

import java.util.List;

@SuppressWarnings("javadoc")
public class VarArgConstructor {

    private final String indicator;

    public VarArgConstructor(final int x, final boolean y, final List<String> z) {
        indicator = "non-vararg";
    }

    public VarArgConstructor(final int x, final boolean y, final String... z) {
        indicator = "vararg";
    }

    public String getIndicator() {
        return indicator;
    }
}
