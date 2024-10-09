

package gc.testlibrary;

public class Allocation {
    public static volatile Object obj;

    

    public static void blackHole(Object obj) {
        Allocation.obj = obj;
        Allocation.obj = null;
    }
}
