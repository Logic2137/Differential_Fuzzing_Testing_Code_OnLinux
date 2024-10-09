

public class Fields {
    private static final Object STATIC_FINAL = new Object();
    private static Object STATIC_NON_FINAL = new Object();
    private final Object FINAL = new Object();
    private Object NON_FINAL = new Object();

    public String name() {
        return this.getClass().getName();
    }
}
