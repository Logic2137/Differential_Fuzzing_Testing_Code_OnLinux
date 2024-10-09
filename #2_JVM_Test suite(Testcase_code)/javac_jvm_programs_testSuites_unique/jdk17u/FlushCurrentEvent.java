import java.awt.EventQueue;
import java.awt.Robot;
import java.util.concurrent.atomic.AtomicBoolean;

public final class FlushCurrentEvent {

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        AtomicBoolean done = new AtomicBoolean();
        EventQueue.invokeLater(() -> {
            robot.delay(15000);
            done.set(true);
        });
        robot.waitForIdle();
        if (!done.get()) {
            throw new RuntimeException("Current event was not flushed");
        }
    }
}
