
package nsk.jvmti.ClassFileLoadHook;

public class classfloadhk005r {

    static long staticField = 0;

    static {
        staticField = 200;
    }

    int intField;

    long longField;

    public classfloadhk005r(int n, long m) {
        intField = n;
        longField = m;
    }

    public int intMethod(int i) {
        return (i + intField);
    }

    public long longMethod(int i) {
        return intMethod(i) * longField;
    }

    public static long testedStaticMethod() {
        classfloadhk005r obj = new classfloadhk005r(10, 20);
        return obj.longMethod(90) + staticField;
    }
}
