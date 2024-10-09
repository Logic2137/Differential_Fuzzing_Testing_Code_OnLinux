

package jdk.jfr.api.metadata.annotations;

import jdk.jfr.Event;
import jdk.jfr.Recording;
import jdk.jfr.Registered;


public class TestRegisteredFalseAndRunning {
    @Registered(false)
    static class NoAutoEvent extends Event {
        String hello;
    }

    public static void main(String... args) {
        try (Recording r = new Recording()) {
            r.start();
            NoAutoEvent event = new NoAutoEvent();
            event.commit();
        }
    }
}
