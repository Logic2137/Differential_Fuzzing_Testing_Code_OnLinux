public class DoOverflow {

    static int count;

    public void overflow() {
        count += 1;
        overflow();
    }

    public static void printAlive() {
        System.out.println("Java thread is alive.");
    }

    public static void printIt() {
        System.out.println("Going to overflow stack");
        try {
            new DoOverflow().overflow();
        } catch (java.lang.StackOverflowError e) {
            System.out.println("Test PASSED. Got StackOverflowError at " + count + " iteration");
        }
    }
}
