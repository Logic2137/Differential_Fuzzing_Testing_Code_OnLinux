



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import sun.rmi.server.MarshalInputStream;


public class UseCodebaseOnlyDefault {
    static final String USAGE = "usage: UseCodebaseOnlyDefault boolean";
    static final String PROPNAME = "java.rmi.server.useCodebaseOnly";

    
    static boolean getActualValue() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject("foo");
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        MarshalInputStream mis = new MarshalInputStream(bais);

        Field f = MarshalInputStream.class.getDeclaredField("useCodebaseOnly");
        f.setAccessible(true);
        return f.getBoolean(mis);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException(USAGE);
        }

        boolean expected;
        if (args[0].equals("true")) {
            expected = true;
        } else if (args[0].equals("false")) {
            expected = false;
        } else {
            throw new IllegalArgumentException(USAGE);
        }
        System.out.println("expected = " + expected);

        String prop = System.getProperty(PROPNAME);
        System.out.print("Property " + PROPNAME);
        if (prop == null) {
            System.out.println(" is not set");
        } else {
            System.out.println(" = '" + prop + "'");
        }

        boolean actual = getActualValue();
        System.out.println("actual = " + actual);

        if (expected != actual)
            throw new AssertionError("actual does not match expected value");
    }
}
