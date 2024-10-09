



import java.io.IOException;

public class UnixDomainSocket {

    static {
        System.loadLibrary("InheritedChannel");
        init();
    }

    private final int fd;
    private volatile String name;

    public UnixDomainSocket() throws IOException {
        this.fd = create();
    }

    public void bind(String name) throws IOException {
        bind0(fd, name);
        this.name = name;
    }

    public UnixDomainSocket accept() throws IOException {
        int newsock = accept0(fd);
        return new UnixDomainSocket(newsock);
    }

    public UnixDomainSocket(int fd) {
        this.fd = fd;
    }

    public void connect(String dest) throws IOException {
        connect0(fd, dest);
    }

    public int read() throws IOException {
        return read0(fd);
    }

    public String name() {
        return name;
    }

    public void write(int w) throws IOException {
        write0(fd, w);
    }

    public void close() {
        close0(fd, name); 
    }

    public int fd() {
        return fd;
    }

    public String toString() {
        return "UnixDomainSocket: fd=" + Integer.toString(fd);
    }

    private static native int create() throws IOException;
    private static native void bind0(int fd, String name) throws IOException;
    private static native int accept0(int fd) throws IOException;
    private static native int connect0(int fd, String name) throws IOException;

    

    private static native int read0(int fd) throws IOException;
    private static native void write0(int fd, int w) throws IOException;
    private static native void close0(int fd, String name);
    private static native void init();
    public static native UnixDomainSocket[] socketpair();
}

