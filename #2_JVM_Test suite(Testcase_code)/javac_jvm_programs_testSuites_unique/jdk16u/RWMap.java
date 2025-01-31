



import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class RWMap implements Map {
    private final Map m;
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public RWMap(Map m) {
        if (m == null)
            throw new NullPointerException();
        this.m = m;
    }

    public RWMap() {
        this(new TreeMap()); 
    }

    public int size() {
        rwl.readLock().lock();
        try { return m.size(); }
        finally { rwl.readLock().unlock(); }
    }

    public boolean isEmpty() {
        rwl.readLock().lock();
        try { return m.isEmpty(); }
        finally { rwl.readLock().unlock(); }
    }

    public Object get(Object key) {
        rwl.readLock().lock();
        try { return m.get(key); }
        finally { rwl.readLock().unlock(); }
    }

    public boolean containsKey(Object key) {
        rwl.readLock().lock();
        try { return m.containsKey(key); }
        finally { rwl.readLock().unlock(); }
    }

    public boolean containsValue(Object value) {
        rwl.readLock().lock();
        try { return m.containsValue(value); }
        finally { rwl.readLock().unlock(); }
    }

    public Set keySet() { 
        return null;
    }

    public Set entrySet() { 
        return null;
    }

    public Collection values() { 
        return null;
    }

    public boolean equals(Object o) {
        rwl.readLock().lock();
        try { return m.equals(o); }
        finally { rwl.readLock().unlock(); }
    }

    public int hashCode() {
        rwl.readLock().lock();
        try { return m.hashCode(); }
        finally { rwl.readLock().unlock(); }
    }

    public String toString() {
        rwl.readLock().lock();
        try { return m.toString(); }
        finally { rwl.readLock().unlock(); }
    }

    public Object put(Object key, Object value) {
        rwl.writeLock().lock();
        try { return m.put(key, value); }
        finally { rwl.writeLock().unlock(); }
    }

    public Object remove(Object key) {
        rwl.writeLock().lock();
        try { return m.remove(key); }
        finally { rwl.writeLock().unlock(); }
    }

    public void putAll(Map map) {
        rwl.writeLock().lock();
        try { m.putAll(map); }
        finally { rwl.writeLock().unlock(); }
    }

    public void clear() {
        rwl.writeLock().lock();
        try { m.clear(); }
        finally { rwl.writeLock().unlock(); }
    }

}
