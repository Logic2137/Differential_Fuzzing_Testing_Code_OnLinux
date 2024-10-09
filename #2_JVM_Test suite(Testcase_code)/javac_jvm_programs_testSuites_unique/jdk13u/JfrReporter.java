import java.nio.file.Path;
import java.nio.file.Paths;
import jdk.jfr.Recording;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

public class JfrReporter {

    public static void main(String[] args) throws Exception {
        String eventName = args[0];
        try (Recording r = new Recording()) {
            r.enable(eventName);
            r.start();
            r.stop();
            Path p = Paths.get("/", "tmp", eventName + ".jfr");
            r.dump(p);
            for (RecordedEvent e : RecordingFile.readAllEvents(p)) {
                System.out.println("===== EventType: " + e.getEventType().getName());
                for (ValueDescriptor v : e.getEventType().getFields()) {
                    System.out.println(v.getName() + " = " + e.getValue(v.getName()));
                }
            }
        }
    }
}
