

package nsk.jvmti.ClassFileLoadHook;


public class classfloadhk008r {
    static long staticField = 0;

    static {
        staticField = 2;
    }

    int intField;

    public classfloadhk008r(int n) {
        intField = n;
    }

    public long longMethod(int i) {
        return (i + intField) * 2;
    }

    
    public static long testedStaticMethod() {
        classfloadhk008r obj = new classfloadhk008r(10);
        return obj.longMethod(20) - staticField;
    }
}
