import java.lang.ref.WeakReference;
import java.io.File;
import java.nio.file.Paths;
import java.net.*;
import java.util.*;
import javax.management.*;
import javax.management.loading.*;

public class ClassLeakTest {

    public static void main(String[] args) throws Exception {
        System.out.println("Testing that registering and unregistering a " + "Standard MBean does not retain a reference to " + "the MBean's class");
        String[] cpaths = System.getProperty("test.classes", ".").split(File.pathSeparator);
        URL[] urls = new URL[cpaths.length];
        for (int i = 0; i < cpaths.length; i++) {
            urls[i] = Paths.get(cpaths[i]).toUri().toURL();
        }
        PrivateMLet mlet = new PrivateMLet(urls, null, false);
        Class<?> shadowClass = mlet.loadClass(TestMBean.class.getName());
        if (shadowClass == TestMBean.class) {
            System.out.println("TEST INVALID: MLet got original " + "TestMBean not shadow");
            System.exit(1);
        }
        shadowClass = null;
        MBeanServer mbs = MBeanServerFactory.createMBeanServer();
        ObjectName mletName = new ObjectName("x:type=mlet");
        mbs.registerMBean(mlet, mletName);
        ObjectName testName = new ObjectName("x:type=test");
        mbs.createMBean(Test.class.getName(), testName, mletName);
        ClassLoader testLoader = mbs.getClassLoaderFor(testName);
        if (testLoader != mlet) {
            System.out.println("TEST INVALID: MBean's class loader is not " + "MLet: " + testLoader);
            System.exit(1);
        }
        testLoader = null;
        MBeanInfo info = mbs.getMBeanInfo(testName);
        MBeanAttributeInfo[] attrs = info.getAttributes();
        if (attrs.length != 1 || !attrs[0].getName().equals("A") || !attrs[0].isReadable() || !attrs[0].isWritable() || attrs[0].isIs() || !attrs[0].getType().equals("int")) {
            System.out.println("TEST FAILED: unexpected MBeanInfo attrs");
            System.exit(1);
        }
        MBeanOperationInfo[] ops = info.getOperations();
        if (ops.length != 1 || !ops[0].getName().equals("bogus") || ops[0].getSignature().length > 0 || ops[0].getImpact() != MBeanOperationInfo.UNKNOWN || !ops[0].getReturnType().equals("void")) {
            System.out.println("TEST FAILED: unexpected MBeanInfo ops");
            System.exit(1);
        }
        if (info.getConstructors().length != 2) {
            System.out.println("TEST FAILED: wrong number of constructors " + "in introspected bean: " + Arrays.asList(info.getConstructors()));
            System.exit(1);
        }
        if (!info.getClassName().endsWith("Test")) {
            System.out.println("TEST FAILED: wrong info class name: " + info.getClassName());
            System.exit(1);
        }
        mbs.unregisterMBean(testName);
        mbs.unregisterMBean(mletName);
        WeakReference mletRef = new WeakReference(mlet);
        mlet = null;
        System.out.println("MBean registered and unregistered, waiting for " + "garbage collector to collect class loader");
        for (int i = 0; i < 10000 && mletRef.get() != null; i++) {
            System.gc();
            Thread.sleep(1);
        }
        if (mletRef.get() == null)
            System.out.println("Test passed: class loader was GC'd");
        else {
            System.out.println("TEST FAILED: class loader was not GC'd");
            System.exit(1);
        }
    }

    public static interface TestMBean {

        public void bogus();

        public int getA();

        public void setA(int a);
    }

    public static class Test implements TestMBean {

        public Test() {
        }

        public Test(int x) {
        }

        public void bogus() {
        }

        public int getA() {
            return 0;
        }

        public void setA(int a) {
        }
    }
}
