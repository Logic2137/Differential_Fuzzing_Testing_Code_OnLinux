

package nsk.jvmti.ClassFileLoadHook;


public class classfloadhk007r {
    static long staticField = 0;

    static {
        staticField = 2;
    }

    int intField;

    public classfloadhk007r(int n) {
        intField = n;
    }

    public long longMethod(int i) {
        return (i - intField);
    }

    
    public static long testedStaticMethod() {
        classfloadhk007r obj = new classfloadhk007r(6);
        return obj.longMethod(16) + staticField;
    }
}
