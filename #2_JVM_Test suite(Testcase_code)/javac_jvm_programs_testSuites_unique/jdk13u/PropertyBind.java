
package jdk.nashorn.test.models;

@SuppressWarnings("javadoc")
public class PropertyBind {

    public static int publicStaticInt;

    public static final int publicStaticFinalInt = 2112;

    private static int staticReadWrite;

    private static int staticReadOnly = 1230;

    private static int staticWriteOnly;

    public int publicInt;

    public final int publicFinalInt = 42;

    private int readWrite;

    private final int readOnly = 123;

    private int writeOnly;

    public int getReadWrite() {
        return readWrite;
    }

    public void setReadWrite(final int readWrite) {
        this.readWrite = readWrite;
    }

    public int getReadOnly() {
        return readOnly;
    }

    public void setWriteOnly(final int writeOnly) {
        this.writeOnly = writeOnly;
    }

    public int peekWriteOnly() {
        return writeOnly;
    }

    public static int getStaticReadWrite() {
        return staticReadWrite;
    }

    public static void setStaticReadWrite(final int staticReadWrite) {
        PropertyBind.staticReadWrite = staticReadWrite;
    }

    public static int getStaticReadOnly() {
        return staticReadOnly;
    }

    public static void setStaticWriteOnly(final int staticWriteOnly) {
        PropertyBind.staticWriteOnly = staticWriteOnly;
    }

    public static int peekStaticWriteOnly() {
        return PropertyBind.staticWriteOnly;
    }
}
