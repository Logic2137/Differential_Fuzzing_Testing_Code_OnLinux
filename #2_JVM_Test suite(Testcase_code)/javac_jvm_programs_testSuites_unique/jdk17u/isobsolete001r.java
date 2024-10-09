
package nsk.jvmti.IsMethodObsolete;

public class isobsolete001r {

    public static int testedStaticMethod(int n, isobsolete001r obj) {
        return obj.testedInstanceMethod(n) * 2;
    }

    public int testedInstanceMethod(int n) {
        return n * 3;
    }
}
