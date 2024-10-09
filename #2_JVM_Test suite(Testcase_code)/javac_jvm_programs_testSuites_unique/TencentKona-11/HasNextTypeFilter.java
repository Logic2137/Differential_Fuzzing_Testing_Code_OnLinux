

package stream.XMLStreamFilterTest;

import javax.xml.stream.EventFilter;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

public class HasNextTypeFilter implements EventFilter, StreamFilter {

    protected boolean[] types = new boolean[20];

    public HasNextTypeFilter() {
    }

    public void addType(int type) {
        types[type] = true;
    }

    public boolean accept(XMLEvent e) {
        return types[e.getEventType()];
    }

    public boolean accept(XMLStreamReader r) {
        return types[r.getEventType()];
    }
}
