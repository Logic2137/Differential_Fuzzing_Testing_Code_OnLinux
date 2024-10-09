



import java.io.FileDescriptor;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.DatagramSocketImpl;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.sun.management.UnixOperatingSystemMXBean;

public class UnreferencedMulticastSockets {

    
    final static ArrayDeque<NamedWeak> pendingSockets = new ArrayDeque<>(5);

    
    final static ReferenceQueue<Object> pendingQueue = new ReferenceQueue<>();

    
    static class Server implements Runnable {

        MulticastSocket ss;

        Server() throws IOException {
            ss = new MulticastSocket(0);
            System.out.printf("  DatagramServer addr: %s: %d%n",
                    this.getHost(), this.getPort());
            pendingSockets.add(new NamedWeak(ss, pendingQueue, "serverMulticastSocket"));
            extractRefs(ss, "serverMulticastSocket");
        }

        InetAddress getHost() throws UnknownHostException {
            InetAddress localhost = InetAddress.getByName("localhost"); 
            return localhost;
        }

        int getPort() {
            return ss.getLocalPort();
        }

        
        public void run() {
            try {
                byte[] buffer = new byte[50];
                DatagramPacket p = new DatagramPacket(buffer, buffer.length);
                ss.receive(p);
                buffer[0] += 1;
                ss.send(p);         

                
                ss = null;
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws Exception {

        
        try (MulticastSocket s = new MulticastSocket(0)) {
            
            s.getLocalPort();   
        }

        long fdCount0 = getFdCount();
        listProcFD();

        
        Server svr = new Server();
        Thread thr = new Thread(svr);
        thr.start();

        MulticastSocket client = new MulticastSocket(0);
        System.out.printf("  client bound port: %d%n", client.getLocalPort());
        client.connect(svr.getHost(), svr.getPort());
        pendingSockets.add(new NamedWeak(client, pendingQueue, "clientMulticastSocket"));
        extractRefs(client, "clientMulticastSocket");

        byte[] msg = new byte[1];
        msg[0] = 1;
        DatagramPacket p = new DatagramPacket(msg, msg.length, svr.getHost(), svr.getPort());
        client.send(p);

        p = new DatagramPacket(msg, msg.length);
        client.receive(p);

        System.out.printf("echo received from: %s%n", p.getSocketAddress());
        if (msg[0] != 2) {
            throw new AssertionError("incorrect data received: expected: 2, actual: " + msg[0]);
        }

        

        Object ref;
        int loops = 20;
        while (!pendingSockets.isEmpty() && loops-- > 0) {
            ref = pendingQueue.remove(1000L);
            if (ref != null) {
                pendingSockets.remove(ref);
                System.out.printf("  ref freed: %s, remaining: %d%n", ref, pendingSockets.size());
            } else {
                client = null;
                p = null;
                msg = null;
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

    
    private static void extractRefs(MulticastSocket s, String name) {
        try {

            Field socketImplField = DatagramSocket.class.getDeclaredField("impl");
            socketImplField.setAccessible(true);
            Object socketImpl = socketImplField.get(s);

            Field fileDescriptorField = DatagramSocketImpl.class.getDeclaredField("fd");
            fileDescriptorField.setAccessible(true);
            FileDescriptor fileDescriptor = (FileDescriptor) fileDescriptorField.get(socketImpl);
            extractRefs(fileDescriptor, name);

            Class<?> socketImplClass = socketImpl.getClass();
            System.out.printf("socketImplClass: %s%n", socketImplClass);
            if (socketImplClass.getName().equals("java.net.TwoStacksPlainDatagramSocketImpl")) {
                Field fileDescriptor1Field = socketImplClass.getDeclaredField("fd1");
                fileDescriptor1Field.setAccessible(true);
                FileDescriptor fileDescriptor1 = (FileDescriptor) fileDescriptor1Field.get(socketImpl);
                extractRefs(fileDescriptor1, name + "::twoStacksFd1");

            } else {
                System.out.printf("socketImpl class name not matched: %s != %s%n",
                        socketImplClass.getName(), "java.net.TwoStacksPlainDatagramSocketImpl");
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
            System.out.print(String.format("  %s:: fd: %s, fd: %d, cleanup: %s%n",
                    name, fileDescriptor, rawfd, cleanup));
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
