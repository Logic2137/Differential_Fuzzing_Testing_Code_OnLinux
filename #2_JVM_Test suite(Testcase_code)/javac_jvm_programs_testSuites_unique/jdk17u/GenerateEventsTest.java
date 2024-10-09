
package MyPackage;

public class GenerateEventsTest {

    static native void agent1GenerateEvents();

    static native void agent2SetThread(Thread thread);

    static native boolean agent1FailStatus();

    static native boolean agent2FailStatus();

    public static void main(String[] args) {
        agent2SetThread(Thread.currentThread());
        agent1GenerateEvents();
        if (agent1FailStatus() || agent2FailStatus()) {
            throw new RuntimeException("GenerateEventsTest failed!");
        }
        System.out.println("GenerateEventsTest passed!");
    }
}
