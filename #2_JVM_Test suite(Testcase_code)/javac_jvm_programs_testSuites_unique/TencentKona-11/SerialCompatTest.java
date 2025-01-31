



import java.io.*;
import java.util.*;
import javax.management.ObjectName;

public class SerialCompatTest {

    public static void check6211220() throws Exception {

        ObjectName on = new ObjectName("a:b=c");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(on);
        oos.close();
        byte[] bytes = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        ObjectName on1 = (ObjectName) ois.readObject();

        
        for (int i = 0; i <= 11; i++) {
            String msg = "6211220 case(" + i + ")";
            try {
                switch (i) {
                    case 0:
                        check(msg, on1.getDomain().equals("a"));
                        break;
                    case 1:
                        check(msg, on1.getCanonicalName().equals("a:b=c"));
                        break;
                    case 2:
                        check(msg, on1.getKeyPropertyListString()
                                .equals("b=c"));
                        break;
                    case 3:
                        check(msg, on1.getCanonicalKeyPropertyListString()
                                .equals("b=c"));
                        break;
                    case 4:
                        check(msg, on1.getKeyProperty("b").equals("c"));
                        break;
                    case 5:
                        check(msg, on1.getKeyPropertyList()
                                .equals(Collections.singletonMap("b", "c")));
                        break;
                    case 6:
                        check(msg, !on1.isDomainPattern());
                        break;
                    case 7:
                        check(msg, !on1.isPattern());
                        break;
                    case 8:
                        check(msg, !on1.isPropertyPattern());
                        break;
                    case 9:
                        check(msg, on1.equals(on));
                        break;
                    case 10:
                        check(msg, on.equals(on1));
                        break;
                    case 11:
                        check(msg, on1.apply(on));
                        break;
                    default:
                        throw new Exception(msg + ": Test incorrect");
                }
            } catch (Exception e) {
                System.out.println(msg + ": Test failed with exception:");
                e.printStackTrace(System.out);
                failed = true;
            }
        }

        if (failed) {
            throw new Exception("Some tests for 6211220 failed");
        } else {
            System.out.println("All tests for 6211220 passed");
        }
    }

    static void checkName(String testname, ObjectName on)
            throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(on);
        oos.close();
        byte[] bytes = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        ObjectName on1 = (ObjectName) ois.readObject();
        
        for (int i = 0; i <= 11; i++) {
            String msg = testname + " case(" + i + ")";
            try {
                switch (i) {
                    case 0:
                        check(msg, on1.getDomain().equals(on.getDomain()));
                        break;
                    case 1:
                        check(msg, on1.getCanonicalName().
                                equals(on.getCanonicalName()));
                        break;
                    case 2:
                        check(msg, on1.getKeyPropertyListString().
                                equals(on.getKeyPropertyListString()));
                        break;
                    case 3:
                        check(msg, on1.getCanonicalKeyPropertyListString().
                                equals(on.getCanonicalKeyPropertyListString()));
                        break;
                    case 4:
                        for (Object ko : on1.getKeyPropertyList().keySet()) {
                            final String key = (String) ko;
                            check(msg, on1.getKeyProperty(key).
                                    equals(on.getKeyProperty(key)));
                        }
                        for (Object ko : on.getKeyPropertyList().keySet()) {
                            final String key = (String) ko;
                            check(msg, on1.getKeyProperty(key).
                                    equals(on.getKeyProperty(key)));
                        }
                    case 5:
                        check(msg, on1.getKeyPropertyList()
                                .equals(on.getKeyPropertyList()));
                        break;
                    case 6:
                        check(msg, on1.isDomainPattern()==on.isDomainPattern());
                        break;
                    case 7:
                        check(msg, on1.isPattern() == on.isPattern());
                        break;
                    case 8:
                        check(msg,
                              on1.isPropertyPattern()==on.isPropertyPattern());
                        break;
                    case 9:
                        check(msg, on1.equals(on));
                        break;
                    case 10:
                        check(msg, on.equals(on1));
                        break;
                    case 11:
                        if (!on.isPattern()) {
                            check(msg, on1.apply(on));
                        }
                        break;
                    default:
                        throw new Exception("Test incorrect: case: " + i);
                }
            } catch (Exception e) {
                System.out.println("Test (" + i + ") failed with exception:");
                e.printStackTrace(System.out);
                failed = true;
            }
        }

    }
    private static String[] names6616825 = {
        "a:b=c", "a:b=c,*", "*:*", ":*", ":b=c", ":b=c,*",
        "a:*,b=c", ":*", ":*,b=c", "*x?:k=\"x\\*z\"", "*x?:k=\"x\\*z\",*",
        "*x?:*,k=\"x\\*z\"", "*x?:k=\"x\\*z\",*,b=c"
    };

    static void check6616825() throws Exception {
        System.out.println("Testing 616825");
        for (String n : names6616825) {
            final ObjectName on;
            try {
                on = new ObjectName(n);
            } catch (Exception x) {
                failed = true;
                System.out.println("Unexpected failure for 6616825 [" + n +
                        "]: " + x);
                x.printStackTrace(System.out);
                continue;
            }
            try {
                checkName("616825 " + n, on);
            } catch (Exception x) {
                failed = true;
                System.out.println("6616825 failed for [" + n + "]: " + x);
                x.printStackTrace(System.out);
            }
        }

        if (failed) {
            throw new Exception("Some tests for 6616825 failed");
        } else {
            System.out.println("All tests for 6616825 passed");
        }
    }

    public static void main(String[] args) throws Exception {
        
        ObjectStreamClass osc = ObjectStreamClass.lookup(ObjectName.class);
        if (osc.getFields().length != 6) {
            throw new Exception("Not using old serial form: fields: " +
                    Arrays.asList(osc.getFields()));
        
        }

        try {
            check6211220();
        } catch (Exception x) {
            System.err.println(x.getMessage());
        }
        try {
            check6616825();
        } catch (Exception x) {
            System.err.println(x.getMessage());
        }

        if (failed) {
            throw new Exception("Some tests failed");
        } else {
            System.out.println("All tests passed");
        }
    }

    private static void check(String msg, boolean condition) {
        if (!condition) {
            new Throwable("Test failed " + msg).printStackTrace(System.out);
            failed = true;
        }
    }
    private static boolean failed;
}
