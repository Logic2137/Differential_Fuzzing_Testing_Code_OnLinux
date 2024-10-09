
package MyPackage;

public class VMEventRecursionTest implements Cloneable {

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args) {
        VMEventRecursionTest obj = new VMEventRecursionTest();
        try {
            obj.clone();
        } catch (CloneNotSupportedException e) {
        }
    }
}
