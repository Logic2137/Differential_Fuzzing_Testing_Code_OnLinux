
package nsk.share.test;

public class LazyFormatString {

    private String format;
    private Object[] args;

    public LazyFormatString(String format, Object... args) {
        this.format = format;
        this.args = args;
    }

    @Override
    public String toString() {
        return String.format(format, args);
    }
}
