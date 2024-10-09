



import java.io.FileDescriptor;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.sun.management.UnixOperatingSystemMXBean;

public class UnreferencedSockets {

    
    final static ArrayDeque<NamedWeak> pendingSockets = new ArrayDeque<>(100);

    
    final static ReferenceQueue<Object> pendingQueue = new ReferenceQueue<>();

    
    static class Server implements Runnable {

        ServerSocket ss;

        Server() throws IOException {
            ss = new ServerSocket(0);
            pendingSockets.add(new NamedWeak(ss, pendingQueue, "serverSocket"));
            extractRefs(ss, "serverSocket");
        }

        public int localPort() {
            return ss.getLocalPort();
        }


        public void run() {
            try {
                Socket s = ss.accept();
                pendingSockets.add(new NamedWeak(s, pendingQueue, "acceptedSocket"));
                extractRefs(s, "acceptedSocket");

                InputStream in = s.getInputStream();
                int b = in.read();
                OutputStream out = s.getOutputStream();
                out.write(b);
                
                out = null;
                in = null;
                s = null;
            } catch (Exception ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    ss.close();
                    ss = null;
                } catch (IOException x) {
                    x.printStackTrace();
                }
            }
        }
    }

    public static void main(String args[]) throws Exception {

        
        try (ServerSocket s = new ServerSocket(0)) {
            
            s.getLocalPort();   
        }

        long fdCount0 = getFdCount();
        listProcFD();

        
        Server svr = new Server();
        Thread thr = new Thread(svr);
        thr.start();

        Socket s = new Socket("localhost", svr.localPort());
        pendingSockets.add(new NamedWeak(s, pendingQueue, "clientSocket"));
        extractRefs(s, "clientSocket");

        OutputStream out = s.getOutputStream();
        out.write('x');
        out.flush();
        InputStream in = s.getInputStream();
        int b = in.read();  
        System.out.printf("  data sent and received%n");
        

        Object ref;
        int loops = 20;
        while (!pendingSockets.isEmpty() && loops-- > 0) {
            ref = pendingQueue.remove(1000L);
            if (ref != null) {
                pendingSockets.remove(ref);
                System.out.printf("  ref queued: %s, remaining: %d%n", ref, pendingSockets.size());
            } else {
                s = null;
                out = null;
                in = null;
                System.gc();
            }
        }

        thr.join();

        
        long fdCount = getFdCount();
        System.out.printf("Initial fdCount: %d, final fdCount: %d%n", fdCount0, fdCount);
        listProcFD();

        if (loops == 0) {
            throw new AssertionError("Not all references reclaimed");
        }
    }

    
    private static long getFdCount() {
        OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
        return (mxBean instanceof UnixOperatingSystemMXBean)
                ? ((UnixOperatingSystemMXBean) mxBean).getOpenFileDescriptorCount()
                : -1L;
    }

    
    private static void extractRefs(Socket s, String name) {
        try {

            Field socketImplField = Socket.class.getDeclaredField("impl");
            socketImplField.setAccessible(true);
            Object socketImpl = socketImplField.get(s);

            Field fileDescriptorField = SocketImpl.class.getDeclaredField("fd");
            fileDescriptorField.setAccessible(true);
            FileDescriptor fileDescriptor = (FileDescriptor) fileDescriptorField.get(socketImpl);
            extractRefs(fileDescriptor, name);

            Class<?> socketImplClass = socketImpl.getClass();
            System.out.printf("socketImplClass: %s%n", socketImplClass);
            if (socketImplClass.getClass().getName().equals("java.net.TwoStacksPlainSocketImpl")) {
                Field fileDescriptor1Field = socketImplClass.getDeclaredField("fd1");
                fileDescriptor1Field.setAccessible(true);
                FileDescriptor fileDescriptor1 = (FileDescriptor) fileDescriptor1Field.get(socketImpl);
                extractRefs(fileDescriptor1, name + "::twoStacksFd1");

            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
            throw new AssertionError("missing field", ex);
        }
    }

    private static void extractRefs(FileDescriptor fileDescriptor, String name) {
        Object cleanup = null;
        int rawfd = -1;
        try {
            if (fileDescriptor != null) {
                Field fd1Field = FileDescriptor.class.getDeclaredField("fd");
                fd1Field.setAccessible(true);
                rawfd = fd1Field.getInt(fileDescriptor);

                Field cleanupfdField = FileDescriptor.class.getDeclaredField("cleanup");
                cleanupfdField.setAccessible(true);
                cleanup = cleanupfdField.get(fileDescriptor);
                pendingSockets.add(new NamedWeak(fileDescriptor, pendingQueue,
                        name + "::fileDescriptor: " + rawfd));
                pendingSockets.add(new NamedWeak(cleanup, pendingQueue, name + "::fdCleanup: " + rawfd));

            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
            throw new AssertionError("missing field", ex);
        } finally {
            System.out.print(String.format("  fd: %s, fd: %d, cleanup: %s%n",
                    fileDescriptor, rawfd, cleanup));
        }
    }

    private static void extractRefs(ServerSocket s, String name) {
        try {

            Field socketImplField = ServerSocket.class.getDeclaredField("impl");
            socketImplField.setAccessible(true);
            Object socketImpl = socketImplField.get(s);

            Field fileDescriptorField = SocketImpl.class.getDeclaredField("fd");
            fileDescriptorField.setAccessible(true);
            FileDescriptor fileDescriptor = (FileDescriptor) fileDescriptorField.get(socketImpl);

            Field fdField = FileDescriptor.class.getDeclaredField("fd");
            fdField.setAccessible(true);
            int rawfd = fdField.getInt(fileDescriptor);

            Field cleanupField = FileDescriptor.class.getDeclaredField("cleanup");
            cleanupField.setAccessible(true);
            Object cleanup = cleanupField.get(fileDescriptor);

            System.out.print(String.format("  fd: %s, fd: %d, cleanup: %s, socket: %s%n",
                    fileDescriptor, rawfd, cleanup, s));

            pendingSockets.add(new NamedWeak(fileDescriptor, pendingQueue,
                    name + "::fileDescriptor: " + rawfd));
            pendingSockets.add(new NamedWeak(cleanup, pendingQueue, name + "::fdCleanup: " + rawfd));

        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
            throw new AssertionError("missing field", ex);
        }
    }

    
    static void listProcFD() {
        List<String> lsofDirs = List.of("/usr/bin", "/usr/sbin");
        Optional<Path> lsof = lsofDirs.stream()
                .map(s -> Paths.get(s, "lsof"))
                .filter(f -> Files.isExecutable(f))
                .findFirst();
        lsof.ifPresent(exe -> {
            try {
                System.out.printf("Open File Descriptors:%n");
                long pid = ProcessHandle.current().pid();
                ProcessBuilder pb = new ProcessBuilder(exe.toString(), "-p", Integer.toString((int) pid));
                pb.inheritIO();
                Process p = pb.start();
                p.waitFor(10, TimeUnit.SECONDS);
            } catch (IOException | InterruptedException ie) {
                ie.printStackTrace();
            }
        });
    }

    
    static class NamedWeak extends WeakReference<Object> {
        private final String name;

        NamedWeak(Object o, ReferenceQueue<Object> queue, String name) {
            super(o, queue);
            this.name = name;
        }

        public String toString() {
            return name + "; " + super.toString();
        }
    }
}
