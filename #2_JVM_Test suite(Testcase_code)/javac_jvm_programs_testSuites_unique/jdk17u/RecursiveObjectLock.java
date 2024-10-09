public class RecursiveObjectLock {

    public void testMethod() {
        synchronized (this) {
            nestedLock1();
        }
    }

    public void nestedLock1() {
        synchronized (this) {
            nestedLock2();
        }
    }

    public void nestedLock2() {
        synchronized (this) {
            callWait();
        }
    }

    public void callWait() {
        try {
            this.wait(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        breakpoint1();
    }

    public static void breakpoint1() {
    }

    public static void main(String[] args) {
        RecursiveObjectLock ro = new RecursiveObjectLock();
        ro.testMethod();
        System.out.println("ready");
    }
}
