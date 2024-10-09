

package jdk.nashorn.test.models;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ArrayConversionPreferences {
    public boolean testCollectionOverMap(final Collection x) { return true; }
    public boolean testCollectionOverMap(final Map x) { return false; }

    public boolean testCollectionOverArray(final Collection x) { return true; }
    public boolean testCollectionOverArray(final Object[] x) { return false; }

    public boolean testListOverMap(final List x) { return true; }
    public boolean testListOverMap(final Map x) { return false; }

    public boolean testListOverArray(final List x) { return true; }
    public boolean testListOverArray(final Object[] x) { return false; }

    public boolean testListOverCollection(final List x) { return true; }
    public boolean testListOverCollection(final Collection x) { return false; }

    public boolean testQueueOverMap(final Queue x) { return true; }
    public boolean testQueueOverMap(final Map x) { return false; }

    public boolean testQueueOverArray(final Queue x) { return true; }
    public boolean testQueueOverArray(final Object[] x) { return false; }

    public boolean testQueueOverCollection(final Queue x) { return true; }
    public boolean testQueueOverCollection(final Collection x) { return false; }

    public boolean testDequeOverMap(final Deque x) { return true; }
    public boolean testDequeOverMap(final Map x) { return false; }

    public boolean testDequeOverArray(final Deque x) { return true; }
    public boolean testDequeOverArray(final Object[] x) { return false; }

    public boolean testDequeOverCollection(final Deque x) { return true; }
    public boolean testDequeOverCollection(final Collection x) { return false; }

    public boolean testDequeOverQueue(final Deque x) { return true; }
    public boolean testDequeOverQueue(final Queue x) { return false; }

    public boolean testArrayOverMap(final Object[] x) { return true; }
    public boolean testArrayOverMap(final Map x) { return false; }
}

