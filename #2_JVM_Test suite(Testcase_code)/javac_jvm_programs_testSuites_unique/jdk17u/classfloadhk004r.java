
package nsk.jvmti.ClassFileLoadHook;

public class classfloadhk004r {

    static long staticField = 0;

    static {
        staticField = 2;
    }

    int intField;

    public classfloadhk004r(int n) {
        intField = n;
    }

    public long longMethod(int i) {
        return (i + intField);
    }

    public static long testedStaticMethod() {
        classfloadhk004r obj = new classfloadhk004r(6);
        return obj.longMethod(4) * staticField;
    }
}
