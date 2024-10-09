
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;


public class TestProxyLazyValue {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(TestProxyLazyValue::testUserProxyLazyValue);
        SwingUtilities.invokeAndWait(TestProxyLazyValue::testProxyLazyValue);
        System.setSecurityManager(new SecurityManager());
        SwingUtilities.invokeAndWait(TestProxyLazyValue::testUserProxyLazyValue);
        SwingUtilities.invokeAndWait(TestProxyLazyValue::testProxyLazyValue);
    }

    private static void testUserProxyLazyValue() {

        Object obj = new UserProxyLazyValue(
                UserLazyClass.class.getName()).createValue(null);

        if (!(obj instanceof UserLazyClass)) {
            throw new RuntimeException("Object is not UserLazyClass!");
        }

        obj = new UserProxyLazyValue(UserLazyClass.class.getName(),
                new Object[]{UserLazyClass.CONSTRUCTOR_ARG}).createValue(null);

        if (!(obj instanceof UserLazyClass)) {
            throw new RuntimeException("Object is not UserLazyClass!");
        }

        if (((UserLazyClass) obj).arg != UserLazyClass.CONSTRUCTOR_ARG) {
            throw new RuntimeException("Constructt argument is wrong!");
        }

        obj = new UserProxyLazyValue(UserLazyClass.class.getName(),
                "method1").createValue(null);

        if (!UserLazyClass.RESULT_1.equals(obj)) {
            throw new RuntimeException("Result is wrong!");
        }

        obj = new UserProxyLazyValue(UserLazyClass.class.getName(),
                "method2", new Object[]{UserLazyClass.RESULT_2}).createValue(null);

        if (!UserLazyClass.RESULT_2.equals(obj)) {
            throw new RuntimeException("Result is wrong!");
        }
    }

    private static void testProxyLazyValue() {

        Object obj = new UIDefaults.ProxyLazyValue(
                UserLazyClass.class.getName()).createValue(null);

        if (!(obj instanceof UserLazyClass)) {
            throw new RuntimeException("Object is not UserLazyClass!");
        }

        obj = new UIDefaults.ProxyLazyValue(UserLazyClass.class.getName(),
                new Object[]{UserLazyClass.CONSTRUCTOR_ARG}).createValue(null);

        if (!(obj instanceof UserLazyClass)) {
            throw new RuntimeException("Object is not UserLazyClass!");
        }

        if (((UserLazyClass) obj).arg != UserLazyClass.CONSTRUCTOR_ARG) {
            throw new RuntimeException("Constructt argument is wrong!");
        }

        obj = new UIDefaults.ProxyLazyValue(UserLazyClass.class.getName(),
                "method1").createValue(null);

        if (!UserLazyClass.RESULT_1.equals(obj)) {
            throw new RuntimeException("Result is wrong!");
        }

        obj = new UIDefaults.ProxyLazyValue(UserLazyClass.class.getName(),
                "method2", new Object[]{UserLazyClass.RESULT_2}).createValue(null);

        if (!UserLazyClass.RESULT_2.equals(obj)) {
            throw new RuntimeException("Result is wrong!");
        }
    }

    public static class UserLazyClass {

        static final int CONSTRUCTOR_ARG = 100;
        static final String RESULT_1 = "1";
        static final String RESULT_2 = "2";

        int arg;

        public UserLazyClass() {
        }

        public UserLazyClass(int arg) {
            this.arg = arg;
        }

        public static String method1() {
            return RESULT_1;
        }

        public static String method2(String arg) {
            return arg;
        }
    }

    public static class UserProxyLazyValue extends UIDefaults.ProxyLazyValue {

        public UserProxyLazyValue(String className) {
            super(className);
        }

        public UserProxyLazyValue(String className, Object[] constructorArgs) {
            super(className, constructorArgs);
        }

        public UserProxyLazyValue(String className, String methodName) {
            super(className, methodName);
        }

        public UserProxyLazyValue(String className, String methodName,
                Object[] methodArgs) {
            super(className, methodName, methodArgs);
        }
    }
}
