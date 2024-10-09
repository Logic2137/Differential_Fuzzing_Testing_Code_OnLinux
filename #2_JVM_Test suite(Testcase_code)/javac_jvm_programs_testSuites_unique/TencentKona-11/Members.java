

public class Members {
    public Members() {}
    protected Members(boolean b) {}
    private Members(int i) {}

    public void publicMethod() {}
    protected void protectedMethod() {}
    private void privateMethod() {}

    public Object publicField = new Object();
    protected Object protectedField = new Object();
    private Object privateField = new Object();

    public final int publicFinalField = 10;
    private final int privateFinalField = 10;
    public static final int publicStaticFinalField = 10;
    private static final int privateStaticFinalField = 10;
}
