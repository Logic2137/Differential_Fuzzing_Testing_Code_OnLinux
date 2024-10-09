
package jdk.nashorn.test.models;

public class OverloadedSetter {
    private String color;

    public void setColor(final int x) {
        this.color = Integer.toString(x);
    }

    public void setColor(final String x) {
        this.color = x;
    }

    public String peekColor() {
        return color;
    }

    public void foo(final int x) {
    }

    public String foo(final String x) {
        return "boo";
    }
}
