

package nsk.jvmti.ClassFileLoadHook;


public class classfloadhk006r {
    static long staticField = 0;

    static {
        staticField = 2;
    }

    int intField;

    public classfloadhk006r(int n) {
        intField = n;
    }

    public long longMethod(int i) {
        return (i - intField);
    }

    
    public static long testedStaticMethod() {
        classfloadhk006r obj = new classfloadhk006r(6);
        return obj.longMethod(16) + staticField;
    }
}
