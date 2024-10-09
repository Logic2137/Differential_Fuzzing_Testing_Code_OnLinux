


interface IClone extends Cloneable {
    void finalize() throws Throwable;
    Object clone();
}

interface ICloneExtend extends IClone { }

public class InterfaceObjectTest implements ICloneExtend {

    public Object clone() {
        System.out.println("In InterfaceObjectTest's clone() method\n");
        return null;
    }

    public void finalize() throws Throwable {
        try {
            System.out.println("In InterfaceObjectTest's finalize() method\n");
        } catch (Throwable t) {
            throw new AssertionError(t);
        }
    }

    public static void tryIt(ICloneExtend o1) {
        try {
            Object o2 = o1.clone();
            o1.finalize();
        } catch (Throwable t) {
            if (t instanceof IllegalAccessError) {
                System.out.println("TEST FAILS - IAE resulted\n");
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        InterfaceObjectTest o1 = new InterfaceObjectTest();
        tryIt(o1);
        System.out.println("TEST PASSES - no IAE resulted\n");
    }
}
