import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;
import java.nio.channels.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.*;

public class Lock {

    static final Random rand = new Random();

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("-lockworker")) {
            int port = Integer.parseInt(args[1]);
            runLockWorker(port);
            System.exit(0);
        }
        LockWorkerMirror worker = startLockWorker();
        try {
            File blah = File.createTempFile("blah", null);
            blah.deleteOnExit();
            testLockProtocol(blah, worker);
            testAsyncClose(blah, worker);
            blah.delete();
        } finally {
            worker.shutdown();
        }
    }

    static void testLockProtocol(File file, LockWorkerMirror worker) throws Exception {
        FileLock fl;
        worker.open(file.getPath()).lock();
        AsynchronousFileChannel ch = AsynchronousFileChannel.open(file.toPath(), READ, WRITE);
        Future<FileLock> result = ch.lock();
        try {
            result.get(2, TimeUnit.SECONDS);
            throw new RuntimeException("Timeout expected");
        } catch (TimeoutException x) {
        }
        worker.unlock();
        fl = result.get();
        fl.release();
        worker.lock(0, 10, false);
        fl = ch.lock(10, 10, false).get();
        fl.release();
        ch.close();
        worker.close();
    }

    static void testAsyncClose(File file, LockWorkerMirror worker) throws Exception {
        worker.open(file.getPath()).lock();
        for (int i = 0; i < 100; i++) {
            AsynchronousFileChannel ch = AsynchronousFileChannel.open(file.toPath(), READ, WRITE);
            Future<FileLock> result = ch.lock();
            try {
                result.get(rand.nextInt(100), TimeUnit.MILLISECONDS);
                throw new RuntimeException("Timeout expected");
            } catch (TimeoutException x) {
            }
            ch.close();
            try {
                result.get();
                throw new RuntimeException("ExecutionException expected");
            } catch (ExecutionException x) {
                if (!(x.getCause() instanceof AsynchronousCloseException)) {
                    x.getCause().printStackTrace();
                    throw new RuntimeException("AsynchronousCloseException expected");
                }
            }
        }
        worker.close();
    }

    static LockWorkerMirror startLockWorker() throws Exception {
        ServerSocketChannel ssc = ServerSocketChannel.open().bind(new InetSocketAddress(0));
        int port = ((InetSocketAddress) (ssc.getLocalAddress())).getPort();
        String sep = FileSystems.getDefault().getSeparator();
        String command = System.getProperty("java.home") + sep + "bin" + sep + "java";
        String testClasses = System.getProperty("test.classes");
        if (testClasses != null)
            command += " -cp " + testClasses;
        command += " Lock -lockworker " + port;
        Process p = Runtime.getRuntime().exec(command);
        IOHandler.handle(p.getInputStream());
        IOHandler.handle(p.getErrorStream());
        SocketChannel sc = ssc.accept();
        return new LockWorkerMirror(sc);
    }

    static final String OPEN_CMD = "open";

    static final String CLOSE_CMD = "close";

    static final String LOCK_CMD = "lock";

    static final String UNLOCK_CMD = "unlock";

    static final char TERMINATOR = ';';

    static class LockWorkerMirror {

        private final SocketChannel sc;

        LockWorkerMirror(SocketChannel sc) {
            this.sc = sc;
        }

        private void sendCommand(String cmd, String... params) throws IOException {
            for (String s : params) {
                cmd += " " + s;
            }
            cmd += TERMINATOR;
            ByteBuffer buf = Charset.defaultCharset().encode(cmd);
            while (buf.hasRemaining()) {
                sc.write(buf);
            }
            buf = ByteBuffer.allocate(1);
            int n = sc.read(buf);
            if (n != 1)
                throw new RuntimeException("Reply expected");
            if (buf.get(0) != TERMINATOR)
                throw new RuntimeException("Terminated expected");
        }

        LockWorkerMirror open(String file) throws IOException {
            sendCommand(OPEN_CMD, file);
            return this;
        }

        void close() throws IOException {
            sendCommand(CLOSE_CMD);
        }

        LockWorkerMirror lock() throws IOException {
            sendCommand(LOCK_CMD);
            return this;
        }

        LockWorkerMirror lock(long position, long size, boolean shared) throws IOException {
            sendCommand(LOCK_CMD, position + "," + size + "," + shared);
            return this;
        }

        LockWorkerMirror unlock() throws IOException {
            sendCommand(UNLOCK_CMD);
            return this;
        }

        void shutdown() throws IOException {
            sc.close();
        }
    }

    static class IOHandler implements Runnable {

        private final InputStream in;

        IOHandler(InputStream in) {
            this.in = in;
        }

        static void handle(InputStream in) {
            IOHandler handler = new IOHandler(in);
            Thread thr = new Thread(handler);
            thr.setDaemon(true);
            thr.start();
        }

        public void run() {
            try {
                byte[] b = new byte[100];
                for (; ; ) {
                    int n = in.read(b);
                    if (n < 0)
                        return;
                    for (int i = 0; i < n; i++) {
                        System.out.print((char) b[i]);
                    }
                }
            } catch (IOException ioe) {
            }
        }
    }

    static void runLockWorker(int port) throws Exception {
        SocketChannel sc = SocketChannel.open(new InetSocketAddress(port));
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        FileChannel fc = null;
        FileLock fl = null;
        try {
            for (; ; ) {
                buf.clear();
                int n, last = 0;
                do {
                    n = sc.read(buf);
                    if (n < 0)
                        return;
                    if (n == 0)
                        throw new AssertionError();
                    last += n;
                } while (buf.get(last - 1) != TERMINATOR);
                buf.flip();
                String s = Charset.defaultCharset().decode(buf).toString();
                int sp = s.indexOf(" ");
                String cmd = (sp < 0) ? s.substring(0, s.length() - 1) : s.substring(0, sp);
                String param = (sp < 0) ? "" : s.substring(sp + 1, s.length() - 1);
                if (cmd.equals(OPEN_CMD)) {
                    if (fc != null)
                        throw new RuntimeException("File already open");
                    fc = FileChannel.open(Paths.get(param), READ, WRITE);
                }
                if (cmd.equals(CLOSE_CMD)) {
                    if (fc == null)
                        throw new RuntimeException("No file open");
                    fc.close();
                    fc = null;
                    fl = null;
                }
                if (cmd.equals(LOCK_CMD)) {
                    if (fl != null)
                        throw new RuntimeException("Already holding lock");
                    if (param.length() == 0) {
                        fl = fc.lock();
                    } else {
                        String[] values = param.split(",");
                        if (values.length != 3)
                            throw new RuntimeException("Lock parameter invalid");
                        long position = Long.parseLong(values[0]);
                        long size = Long.parseLong(values[1]);
                        boolean shared = Boolean.parseBoolean(values[2]);
                        fl = fc.lock(position, size, shared);
                    }
                }
                if (cmd.equals(UNLOCK_CMD)) {
                    if (fl == null)
                        throw new RuntimeException("Not holding lock");
                    fl.release();
                    fl = null;
                }
                byte[] reply = { TERMINATOR };
                n = sc.write(ByteBuffer.wrap(reply));
            }
        } finally {
            sc.close();
            if (fc != null)
                fc.close();
        }
    }
}
