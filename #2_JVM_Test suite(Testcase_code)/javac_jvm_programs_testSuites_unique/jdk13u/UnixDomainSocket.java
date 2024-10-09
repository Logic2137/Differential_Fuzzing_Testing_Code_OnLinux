import java.io.IOException;

public class UnixDomainSocket {

    static {
        System.loadLibrary("InheritedChannel");
        init();
    }

    private final int fd;

    public UnixDomainSocket(int fd) {
        this.fd = fd;
    }

    public int read() throws IOException {
        return read0(fd);
    }

    public void write(int w) throws IOException {
        write0(fd, w);
    }

    public void close() {
        close0(fd);
    }

    public int fd() {
        return fd;
    }

    public String toString() {
        return "UnixDomainSocket: fd=" + Integer.toString(fd);
    }

    private static native int read0(int fd) throws IOException;

    private static native void write0(int fd, int w) throws IOException;

    private static native void close0(int fd);

    private static native void init();

    public static native UnixDomainSocket[] socketpair();
}
