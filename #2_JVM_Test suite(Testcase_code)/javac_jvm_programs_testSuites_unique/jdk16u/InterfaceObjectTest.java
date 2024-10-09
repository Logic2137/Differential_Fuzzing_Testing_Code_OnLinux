


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
            throw new AssertionError(t);
        }
    }


    public static void main(String[] args) throws Exception {
        
        InterfaceObjectTest o1 = new InterfaceObjectTest();
        tryIt(o1);


        
        Class cls = Class.forName("InterfaceObj");
        try {
            java.lang.reflect.Method m = cls.getMethod("testFinalize");
            m.invoke(cls);
            throw new RuntimeException("Failed to throw NoSuchMethodError for finalize()");
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (!e.getCause().toString().contains("NoSuchMethodError")) {
                throw new RuntimeException("wrong ITE: " + e.getCause().toString());
            }
        }

        try {
            java.lang.reflect.Method m = cls.getMethod("testClone");
            m.invoke(cls);
            throw new RuntimeException("Failed to throw NoSuchMethodError for clone()");
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (!e.getCause().toString().contains("NoSuchMethodError")) {
                throw new RuntimeException("wrong ITE: " + e.getCause().toString());
            }
        }

    }
}
