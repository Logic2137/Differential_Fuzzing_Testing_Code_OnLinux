public class TestCodegenAttach {

    static native void testCodegenAttach();

    static {
        System.loadLibrary("codegenAttach");
    }

    public static void main(String[] args) throws Throwable {
        testCodegenAttach();
    }
}
