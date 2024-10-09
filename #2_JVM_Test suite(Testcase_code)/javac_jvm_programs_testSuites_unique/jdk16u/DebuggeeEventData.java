
package nsk.share.jdi;


public class DebuggeeEventData {
    
    public static class DebugEventData {
    }

    
    public static class DebugMonitorEventData extends DebugEventData {
        public DebugMonitorEventData(Object monitor, Thread thread, Object eventObject) {
            this.monitor = monitor;
            this.thread = thread;
            this.eventObject = eventObject;
        }

        public Object monitor;

        public Thread thread;

        public Object eventObject;
    }

    
    public static class DebugMonitorEnterEventData extends DebugMonitorEventData {
        public DebugMonitorEnterEventData(Object monitor, Thread thread, Object eventObject) {
            super(monitor, thread, eventObject);
        }
    }

    
    public static class DebugMonitorEnteredEventData extends DebugMonitorEventData {
        public DebugMonitorEnteredEventData(Object monitor, Thread thread, Object eventObject) {
            super(monitor, thread, eventObject);
        }
    }

    
    public static class DebugMonitorWaitEventData extends DebugMonitorEventData {
        public long timeout;

        public DebugMonitorWaitEventData(Object monitor, Thread thread, long timeout, Object eventObject) {
            super(monitor, thread, eventObject);
            this.timeout = timeout;
        }
    }

    
    public static class DebugMonitorWaitedEventData extends DebugMonitorEventData {
        public boolean timedout;

        public DebugMonitorWaitedEventData(Object monitor, Thread thread, boolean timedout, Object eventObject) {
            super(monitor, thread, eventObject);
            this.timedout = timedout;
        }
    }

}
