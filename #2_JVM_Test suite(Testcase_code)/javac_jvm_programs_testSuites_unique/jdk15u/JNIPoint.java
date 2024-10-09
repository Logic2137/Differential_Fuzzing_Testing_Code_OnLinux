
package org.openjdk.bench.jdk.incubator.foreign.points.support;

public class JNIPoint implements AutoCloseable {

    static {
        System.loadLibrary("JNIPoint");
    }

    private final long peer;

    public JNIPoint(int x, int y) {
        peer = allocate();
        setX(peer, x);
        setY(peer, y);
    }

    public void free() {
        free(peer);
    }

    public int getX() {
        return getX(peer);
    }

    public void setX(int value) {
        setX(peer, value);
    }

    public int getY() {
        return getY(peer);
    }

    public void setY(int value) {
        setY(peer, value);
    }

    private static native long allocate();
    private static native void free(long ptr);

    private static native int getX(long ptr);
    private static native void setX(long ptr, int x);

    private static native int getY(long ptr);
    private static native void setY(long ptr, int y);

    @Override
    public void close() {
        free();
    }
}
