

package jdk.jfr.event.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.ProtectionDomain;
import java.util.concurrent.CountDownLatch;




public class EvilInstrument {

    CountDownLatch socketEchoReady = new CountDownLatch(1);
    ServerSocket ss;

    
    class SocketEcho extends Thread
    {
        public SocketEcho() {
            setDaemon(true);
        }

        public void run() {
            try {
                Socket s = ss.accept();
                OutputStream os = s.getOutputStream();
                InputStream is = s.getInputStream();
                socketEchoReady.countDown();
                for(;;) {
                    int b = is.read();
                    os.write(b);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    public static File createScratchFile() throws IOException {
        return File.createTempFile("EvilTransformer", null, new File(".")).getAbsoluteFile();
    }

    class EvilTransformer implements ClassFileTransformer {
        File scratch;
        Socket s;
        volatile boolean inited = false;

        public EvilTransformer() throws Exception {
            scratch = createScratchFile();
            ss = new ServerSocket(0);
            new SocketEcho().start();
            s = new Socket(ss.getInetAddress(), ss.getLocalPort());
            socketEchoReady.await();
            inited = true;
        }

        public byte[] transform(ClassLoader loader, String className,
                                Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain,
                                byte[] classfileBuffer)
        {
            if (!inited) {
                return null;
            }
            
            try {
                FileOutputStream fos = new FileOutputStream(scratch);
                fos.write(31);
                fos.close();

                FileInputStream fis = new FileInputStream(scratch);
                fis.read();
                fis.close();

                RandomAccessFile raf = new RandomAccessFile(scratch, "rw");
                raf.read();
                raf.write(31);
                raf.close();

                s.getOutputStream().write(31);
                s.getInputStream().read();

            } catch(Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            return null;
        }
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        new EvilInstrument().addTransformer(inst);
    }

    private void addTransformer(Instrumentation inst) {
        try {
            inst.addTransformer(new EvilTransformer());
        } catch(Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String... args) throws Exception {
        System.out.println("Hello");
    }

}
