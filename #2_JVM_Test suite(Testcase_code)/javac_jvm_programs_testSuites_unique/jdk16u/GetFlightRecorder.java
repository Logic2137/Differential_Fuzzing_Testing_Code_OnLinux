

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import jdk.jfr.Event;
import jdk.jfr.EventType;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;
import jdk.jfr.consumer.RecordingFile;
import jdk.jfr.consumer.RecordedEvent;

public class GetFlightRecorder {
    private static class TestEvent extends Event {
    }
    private static class SimpleEvent extends Event {
        public int id;
    }
    public static void main(String args[]) throws Exception {
        EventType type = EventType.getEventType(TestEvent.class); 
        if (type.isEnabled()) {
            throw new RuntimeException("Expected event to be disabled before recording start");
        }

        
        System.out.println("jdk.jfr.FlightRecorder.getFlightRecorder() = " + FlightRecorder.getFlightRecorder());

        
        Recording r = new Recording();
        r.start();
        if (!type.isEnabled()) {
            throw new RuntimeException("Expected event to be enabled during recording");
        }
        TestEvent testEvent = new TestEvent();
        testEvent.commit();
        loadEventClassDuringRecording();
        r.stop();
        if (type.isEnabled()) {
            throw new RuntimeException("Expected event to be disabled after recording stopped");
        }
        System.out.println("Checking SimpleEvent");
        hasEvent(r, SimpleEvent.class.getName());
        System.out.println("OK");

        System.out.println("Checking TestEvent");
        hasEvent(r, TestEvent.class.getName());
        System.out.println("OK");
    }

    
    
    private static void loadEventClassDuringRecording() {
        SimpleEvent event = new SimpleEvent();
        event.commit();
    }

    public static List<RecordedEvent> fromRecording(Recording recording) throws IOException {
        return RecordingFile.readAllEvents(makeCopy(recording));
    }

    private static Path makeCopy(Recording recording) throws IOException {
        Path p = recording.getDestination();
        if (p == null) {
            File directory = new File(".");
            
            
            ProcessHandle h = ProcessHandle.current();
            p = new File(directory.getAbsolutePath(), "recording-" + recording.getId() + "-pid" + h.pid() + ".jfr").toPath();
            recording.dump(p);
        }
        return p;
    }

    public static void hasEvent(Recording r, String name) throws IOException {
        List<RecordedEvent> events = fromRecording(r);
        hasEvents(events);
        hasEvent(events, name);
    }

    public static void hasEvents(List<RecordedEvent> events) {
        if (events.isEmpty()) {
            throw new RuntimeException("No events");
        }
    }

    public static void hasEvent(List<RecordedEvent> events, String name) throws IOException {
        if (!containsEvent(events, name)) {
            throw new RuntimeException("Missing event " + name  + " in recording " + events.toString());
        }
    }

    private static boolean containsEvent(List<RecordedEvent> events, String name) {
        for (RecordedEvent event : events) {
            if (event.getEventType().getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
