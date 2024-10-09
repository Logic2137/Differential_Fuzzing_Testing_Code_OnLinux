

package jdk.jfr.startupargs;

import jdk.jfr.Event;
import jdk.jfr.EventType;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Name;
import jdk.jfr.Recording;


public class TestStartNoSettings {

    @Name("UserEvent")
    static class UserEvent extends Event {
    }

    public static void main(String[] a) throws Exception {
        boolean userEnabled = false;
        try (Recording r = new Recording()) {
            r.start();
            UserEvent e = new UserEvent();
            e.commit();
            for (EventType et : FlightRecorder.getFlightRecorder().getEventTypes()) {
                if (et.isEnabled()) {
                    if (!et.getName().equals("UserEvent")) {
                        throw new Exception("Only 'UserEvent' should be enabled");
                    }
                    userEnabled = true;
                }
            }
        }

        if (!userEnabled)  {
            throw new Exception("Expected 'UserEvent' to be enabled with -XX:StartFlightRecording=settings=none");
        }
    }
}
