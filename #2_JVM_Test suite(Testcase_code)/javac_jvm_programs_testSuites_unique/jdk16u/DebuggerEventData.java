
package nsk.share.jdi;

import com.sun.jdi.*;
import com.sun.jdi.event.*;


public class DebuggerEventData
{
    static abstract class DebugEventData
    {

        protected Class<?> eventClass;
        public DebugEventData(Class<?> eventClass) {
            this.eventClass = eventClass;
        }

        public boolean shouldCheckEvent(Event event) {
            return this.eventClass.isAssignableFrom(event.getClass());
        }

        
        abstract public boolean checkEvent(Event event);
    }

    
    static abstract class DebugMonitorEventData extends DebugEventData {
        protected ObjectReference monitor;

        protected ThreadReference thread;

        public DebugMonitorEventData(Class<?> eventClass, ObjectReference debuggeeMirror) {
            super(eventClass);

            this.eventClass = eventClass;
            monitor = (ObjectReference) debuggeeMirror.getValue(debuggeeMirror.referenceType().fieldByName("monitor"));
            thread = (ThreadReference) debuggeeMirror.getValue(debuggeeMirror.referenceType().fieldByName("thread"));
        }

        public String toString() {
            return eventClass.getName() + " monitor: " + monitor + " thread: " + thread;
        }
    }

    
    static class DebugMonitorEnterEventData extends DebugMonitorEventData {
        public DebugMonitorEnterEventData(ObjectReference debuggeeMirror) {
            super(MonitorContendedEnterEvent.class, debuggeeMirror);
        }

        public boolean checkEvent(Event event) {
            MonitorContendedEnterEvent monitorEnterEvent = (MonitorContendedEnterEvent) event;

            return monitorEnterEvent.monitor().equals(monitor) && monitorEnterEvent.thread().equals(thread);
        }
    }

    
    static class DebugMonitorEnteredEventData extends DebugMonitorEventData {
        public DebugMonitorEnteredEventData(ObjectReference debuggeeMirror) {
            super(MonitorContendedEnteredEvent.class, debuggeeMirror);
        }

        public boolean checkEvent(Event event) {
            MonitorContendedEnteredEvent monitorEnterEvent = (MonitorContendedEnteredEvent) event;

            return monitorEnterEvent.monitor().equals(monitor) && monitorEnterEvent.thread().equals(thread);
        }
    }

    
    static class DebugMonitorWaitEventData extends DebugMonitorEventData {
        private long timeout;

        public DebugMonitorWaitEventData(ObjectReference debuggeeMirror) {
            super(MonitorWaitEvent.class, debuggeeMirror);

            timeout = ((LongValue) debuggeeMirror.getValue(debuggeeMirror.referenceType().fieldByName("timeout"))).longValue();
        }

        public boolean checkEvent(Event event) {
            MonitorWaitEvent monitorWaitEvent = (MonitorWaitEvent) event;

            return monitorWaitEvent.monitor().equals(monitor) && monitorWaitEvent.thread().equals(thread) && (monitorWaitEvent.timeout() == timeout);
        }

        public String toString() {
            return eventClass.getName() + " monitor: " + monitor + " thread: " + thread + " timeout: " + timeout;
        }
    }

    
    static class DebugMonitorWaitedEventData extends DebugMonitorEventData {
        private boolean timedout;

        public DebugMonitorWaitedEventData(ObjectReference debuggeeMirror) {
            super(MonitorWaitedEvent.class, debuggeeMirror);

            timedout = ((BooleanValue) debuggeeMirror.getValue(debuggeeMirror.referenceType().fieldByName("timedout"))).booleanValue();
        }

        public boolean checkEvent(Event event) {
            MonitorWaitedEvent monitorWaitedEvent = (MonitorWaitedEvent) event;

            return monitorWaitedEvent.monitor().equals(monitor) && monitorWaitedEvent.thread().equals(thread)
                    && (monitorWaitedEvent.timedout() == timedout);
        }

        public String toString() {
            return eventClass.getName() + " monitor: " + monitor + " thread: " + thread + " timedout: " + timedout;
        }
    }
}
