

package jdk.jfr.tool;

import java.util.Comparator;
import jdk.jfr.consumer.RecordedEvent;

public class EndTicksComparator implements Comparator<RecordedEvent> {
    public long readEndTicks(RecordedEvent event) {
        long timestamp = event.getLong("startTime");
        if (event.hasField("duration")) {
            timestamp += event.getLong("duration");
        }
        return timestamp;
    }

    @Override
    public int compare(RecordedEvent a, RecordedEvent b) {
        return Long.compare(readEndTicks(a), readEndTicks(b));
    }
}
