
package gc.g1.plab.lib;

public class MemoryConsumer {

    private int capacity;

    private int chunk;

    private Object[] array;

    private int index;

    public MemoryConsumer(int capacity, int chunk) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Items number should be greater than 0.");
        }
        if (chunk <= 0) {
            throw new IllegalArgumentException("Chunk size should be greater than 0.");
        }
        this.capacity = capacity;
        this.chunk = chunk;
        index = 0;
        array = new Object[this.capacity];
    }

    private void store(Object o) {
        if (array == null) {
            throw new RuntimeException("Capacity should be set before storing");
        }
        array[index % capacity] = o;
        ++index;
    }

    public void consume(long memoryToFill) {
        long allocated = 0;
        while (allocated < memoryToFill) {
            store(new byte[chunk]);
            allocated += chunk;
        }
    }

    public void clear() {
        array = null;
    }
}
