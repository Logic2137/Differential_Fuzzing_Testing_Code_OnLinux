

package jdk.jfr.api.flightrecorder;

import jdk.jfr.Event;
import jdk.jfr.FlightRecorder;


public class TestPeriodicEventsSameHook {

    private static class MyEvent extends Event {
    }

    private static class MyHook implements Runnable {
        @Override
        public void run() {
        }
    }

    public static void main(String[] args) throws Exception {
        MyHook hook = new MyHook();
        FlightRecorder.addPeriodicEvent(MyEvent.class, hook);
        try {
            FlightRecorder.addPeriodicEvent(MyEvent.class, hook);
            throw new Exception("Expected IllegalArgumentException when adding same hook twice");
        } catch (IllegalArgumentException iae) {
            if (!iae.getMessage().equals("Hook has already been added")) {
                throw new Exception("Expected IllegalArgumentException with message 'Hook has already been added'");
            }
        }
    }
}
