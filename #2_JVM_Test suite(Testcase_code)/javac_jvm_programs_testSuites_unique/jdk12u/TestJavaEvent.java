

package jdk.jfr.jvm;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import jdk.jfr.AnnotationElement;
import jdk.jfr.Event;
import jdk.jfr.EventType;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;


public class TestJavaEvent {

    private static final int EVENTS_PER_THREAD = 50;
    private static final int THREAD_COUNT = 100;

    public static class MyEvent extends Event {
        float floatValue;
        double doubleValue;
        int intValue;
        long longValue;
        char charValue;
        byte byteValue;
        String stringValue;
        Thread threadValue;
        Class<?> classValue;

        public void setFloatValue(float value) {
            floatValue = value;
        }

        public void setDoubleValue(double value) {
            doubleValue = value;
        }

        public void setIntValue(int value) {
            intValue = value;
        }

        public void setLongValue(long value) {
            longValue = value;
        }

        public void setCharValue(char value) {
            charValue = value;
        }

        public void setByteValue(byte value) {
            byteValue = value;
        }

        public void setStringValue(String value) {
            stringValue = value;
        }

        public void setThreadValue(Thread value) {
            threadValue = value;
        }

        public void setClassValue(Class<?> value) {
            classValue = value;
        }
    }

    public static void main(String... args) throws IOException, InterruptedException {
        Recording r = new Recording();
        r.enable(MyEvent.class).withThreshold(Duration.ofNanos(0)).withoutStackTrace();
        r.start();
        List<Thread> threads = new ArrayList<>();
        for (int n = 0; n < THREAD_COUNT; n++) {
            Thread t = new Thread(TestJavaEvent::emitEvents);
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }

        r.stop();
        
        File file = File.createTempFile("test", ".jfr");
        r.dump(file.toPath());
        int eventCount = 0;
        for (RecordedEvent e : RecordingFile.readAllEvents(file.toPath())) {
            if (e.getEventType().getName().equals(MyEvent.class.getName())) {
                eventCount++;
            }
            System.out.println(e);
        }
        System.out.println("Event count was " + eventCount + ", expected " + THREAD_COUNT * EVENTS_PER_THREAD);
        r.close();
    }

    private static void emitEvents() {
        for (int n = 0; n < EVENTS_PER_THREAD; n++) {
            MyEvent event = new MyEvent();
            event.begin();
            event.end();
            event.setFloatValue(1.12345f);
            event.setDoubleValue(1.234567890);
            event.setIntValue(123456);
            event.setLongValue(1234567890);
            event.setCharValue('c');
            event.setByteValue((byte) 12);
            event.setStringValue("1234567890");
            event.setThreadValue(Thread.currentThread());
            event.setClassValue(Class.class);
            event.commit();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                
                e.printStackTrace();
            }
        }
    }

    static void prettyPrint() {
        for (EventType type : FlightRecorder.getFlightRecorder().getEventTypes()) {
            for (AnnotationElement a : type.getAnnotationElements()) {
                printAnnotation("", a);
            }
            System.out.print("class " + removePackage(type.getName()));
            System.out.print(" extends Event");

            System.out.println(" {");
            List<ValueDescriptor> values = type.getFields();
            for (int i = 0; i < values.size(); i++) {
                ValueDescriptor v = values.get(i);
                for (AnnotationElement a : v.getAnnotationElements()) {
                    printAnnotation("  ", a);
                }
                System.out.println("  " + removePackage(v.getTypeName() + brackets(v.isArray())) + " " + v.getName());
                if (i != values.size() - 1) {
                    System.out.println();
                }
            }
            System.out.println("}");
            System.out.println();
        }
    }

    private static String brackets(boolean isArray) {
        return isArray ? "[]" : "";
    }

    private static String removePackage(String s) {

        int index = s.lastIndexOf(".");
        return s.substring(index + 1);
    }

    private static void printAnnotation(String indent, AnnotationElement a) {
        String name = removePackage(a.getTypeName());
        if (a.getValues().isEmpty()) {
            System.out.println(indent + "@" + name);
            return;
        }
        System.out.print(indent + "@" + name + "(");
        for (Object o : a.getValues()) {
            printAnnotationValue(o);
        }
        System.out.println(")");
    }

    private static void printAnnotationValue(Object o) {
        if (o instanceof String) {
            System.out.print("\"" + o + "\"");
        } else {
            System.out.print(String.valueOf(o));
        }
    }

}
