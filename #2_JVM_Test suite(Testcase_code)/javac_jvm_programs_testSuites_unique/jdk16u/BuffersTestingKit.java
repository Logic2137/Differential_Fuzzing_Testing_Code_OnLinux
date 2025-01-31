
package jdk.internal.net.http.hpack;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.nio.ByteBuffer.allocate;

public final class BuffersTestingKit {

    
    public static ByteBuffer relocate(ByteBuffer buffer, int newPosition,
                                      int newCapacity) {
        int oldPosition = buffer.position();
        int oldLimit = buffer.limit();

        if (newPosition + oldLimit - oldPosition > newCapacity) {
            throw new IllegalArgumentException();
        }

        ByteBuffer result;
        if (buffer.isDirect()) {
            result = ByteBuffer.allocateDirect(newCapacity);
        } else {
            result = allocate(newCapacity);
        }

        result.position(newPosition);
        result.put(buffer).limit(result.position()).position(newPosition);
        buffer.position(oldPosition);

        if (buffer.isReadOnly()) {
            return result.asReadOnlyBuffer();
        }
        return result;
    }

    public static Iterable<? extends ByteBuffer> relocateBuffers(
            Iterable<? extends ByteBuffer> source) {
        return () ->
                new Iterator<ByteBuffer>() {

                    private final Iterator<? extends ByteBuffer> it = source.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public ByteBuffer next() {
                        ByteBuffer buf = it.next();
                        int remaining = buf.remaining();
                        int newCapacity = remaining + random.nextInt(17);
                        int newPosition = random.nextInt(newCapacity - remaining + 1);
                        return relocate(buf, newPosition, newCapacity);
                    }
                };
    }

    
    public static Iterable<? extends ByteBuffer> injectEmptyBuffers(
            Iterable<? extends ByteBuffer> source) {
        return injectEmptyBuffers(source, () -> allocate(0));
    }

    public static Iterable<? extends ByteBuffer> injectEmptyBuffers(
            Iterable<? extends ByteBuffer> source,
            Supplier<? extends ByteBuffer> emptyBufferFactory) {

        return () ->
                new Iterator<ByteBuffer>() {

                    private final Iterator<? extends ByteBuffer> it = source.iterator();
                    private ByteBuffer next = calculateNext();

                    private ByteBuffer calculateNext() {
                        if (random.nextBoolean()) {
                            return emptyBufferFactory.get();
                        } else if (it.hasNext()) {
                            return it.next();
                        } else {
                            return null;
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return next != null;
                    }

                    @Override
                    public ByteBuffer next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        ByteBuffer next = this.next;
                        this.next = calculateNext();
                        return next;
                    }
                };
    }

    public static ByteBuffer concat(Iterable<? extends ByteBuffer> split) {
        return concat(split, ByteBuffer::allocate);
    }

    public static ByteBuffer concat(Iterable<? extends ByteBuffer> split,
                                    Function<? super Integer, ? extends ByteBuffer> concatBufferFactory) {
        int size = 0;
        for (ByteBuffer bb : split) {
            size += bb.remaining();
        }

        ByteBuffer result = concatBufferFactory.apply(size);
        for (ByteBuffer bb : split) {
            result.put(bb);
        }

        result.flip();
        return result;
    }

    public static void forEachSplit(ByteBuffer bb,
                                    Consumer<? super Iterable<? extends ByteBuffer>> action) {
        forEachSplit(bb.remaining(),
                (lengths) -> {
                    int end = bb.position();
                    List<ByteBuffer> buffers = new LinkedList<>();
                    for (int len : lengths) {
                        ByteBuffer d = bb.duplicate();
                        d.position(end);
                        d.limit(end + len);
                        end += len;
                        buffers.add(d);
                    }
                    action.accept(buffers);
                });
    }

    private static void forEachSplit(int n, Consumer<? super Iterable<? extends Integer>> action) {
        forEachSplit(n, new Stack<>(), action);
    }

    private static void forEachSplit(int n, Stack<Integer> path,
                                     Consumer<? super Iterable<? extends Integer>> action) {
        if (n == 0) {
            action.accept(path);
        } else {
            for (int i = 1; i <= n; i++) {
                path.push(i);
                forEachSplit(n - i, path, action);
                path.pop();
            }
        }
    }

    private static final Random random = new Random();

    private BuffersTestingKit() {
        throw new InternalError();
    }













}
