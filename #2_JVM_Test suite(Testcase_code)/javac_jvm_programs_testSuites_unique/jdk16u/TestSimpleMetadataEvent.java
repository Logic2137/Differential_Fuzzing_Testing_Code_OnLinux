

package jdk.jfr.api.metadata.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jdk.jfr.Event;
import jdk.jfr.EventType;
import jdk.jfr.MetadataDefinition;


public class TestSimpleMetadataEvent {

    @MetadataDefinition
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Severity {
        int value() default 50;
    }

    @Severity
    static class MetadataEvent extends Event {
    }

    public static void main(String[] args) throws Exception {
        EventType.getEventType(MetadataEvent.class);
    }
}
