import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FieldAccessWatch {

    private static final String agentLib = "FieldAccessWatch";

    private static class MyItem {
    }

    private static class MyList {

        public List<MyItem> items = new ArrayList<>();
    }

    public static void main(String[] args) throws Exception {
        try {
            System.loadLibrary(agentLib);
        } catch (UnsatisfiedLinkError ex) {
            System.err.println("Failed to load " + agentLib + " lib");
            System.err.println("java.library.path: " + System.getProperty("java.library.path"));
            throw ex;
        }
        if (!initWatchers(MyList.class, MyList.class.getDeclaredField("items"))) {
            throw new RuntimeException("Watchers initializations error");
        }
        MyList list = new MyList();
        test("[1]items.add(0, object)", () -> list.items.add(0, new MyItem()));
        test("[2]items.add(object)", () -> list.items.add(new MyItem()));
        test("[3]items.add(1, object)", () -> list.items.add(1, new MyItem()));
        test("[4]items.add(object)", () -> list.items.add(new MyItem()));
        test("[5]items.add(1, object)", () -> list.items.add(1, new MyItem()));
    }

    private static void log(String msg) {
        System.out.println(msg);
        System.out.flush();
    }

    private static class TestResult {

        public boolean items_access;

        public boolean modCount_access;

        public boolean modCount_modify;

        public boolean size_access;

        public boolean size_modify;

        public boolean elementData_access;

        public void verify() {
            Arrays.stream(this.getClass().getDeclaredFields()).forEach(f -> verify(f));
        }

        private void verify(Field f) {
            try {
                if (!f.getBoolean(this)) {
                    throw new RuntimeException(f.getName() + " notification is missed");
                }
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @FunctionalInterface
    private interface TestAction {

        void apply();
    }

    private static void test(String descr, TestAction action) throws Exception {
        log(descr + ": starting");
        TestResult result = new TestResult();
        if (!startTest(result)) {
            throw new RuntimeException("startTest failed");
        }
        action.apply();
        Thread.sleep(500);
        stopTest();
        result.verify();
        log(descr + ": OK");
    }

    private static native boolean initWatchers(Class cls, Field field);

    private static native boolean startTest(TestResult results);

    private static native void stopTest();
}
