import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GetContentsInterruptedTest implements ClipboardOwner {

    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    final Transferable transferable = new StringSelection("TEXT");

    private final Object o = new Object();

    public static void main(String[] args) throws Exception {
        if (System.getProperty("os.name").startsWith("Mac")) {
            System.out.println("This tests should not be run on OS X. " + "See CR 7124344 for more info");
        } else {
            boolean subTestFlag = false;
            for (String arg : args) {
                if (arg.indexOf("SubTest") != -1)
                    subTestFlag = true;
            }
            if (!subTestFlag) {
                new GetContentsInterruptedTest().performTest();
            } else {
                new GetContentsInterruptedTest().execute();
            }
        }
    }

    private void performTest() throws Exception {
        int retCode;
        String javaPath = System.getProperty("java.home", "");
        String javaClasspath = System.getProperty("java.class.path", ".");
        String command = javaPath + File.separator + "bin" + File.separator + "java -classpath " + javaClasspath + " GetContentsInterruptedTest SubTest";
        System.out.println(command);
        boolean processExit = false;
        clipboard.setContents(transferable, this);
        Process process = Runtime.getRuntime().exec(command);
        synchronized (o) {
            o.wait(10000);
        }
        try {
            retCode = process.exitValue();
            processExit = true;
        } catch (IllegalThreadStateException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[RESULT] : " + "The sub process has cleanly exited : PASS");
        System.out.println("[RESULT] : Child returned: " + retCode);
        InputStream errorStream = process.getErrorStream();
        System.out.println("========= Child process stderr ========");
        dumpStream(errorStream);
        System.out.println("=======================================");
        InputStream processInputStream = process.getInputStream();
        System.out.println("========= Child process output ========");
        dumpStream(processInputStream);
        System.out.println("=======================================");
        if (!processExit) {
            process.destroy();
            throw new RuntimeException("[RESULT] : " + "The sub process has not exited : FAIL");
        }
    }

    public void execute() {
        System.out.println("Hello world");
        final ClipboardOwner clipboardOwner = new ClipboardOwner() {

            public void lostOwnership(Clipboard clipboard, Transferable contents) {
                System.exit(0);
            }
        };
        clipboard.setContents(transferable, clipboardOwner);
        synchronized (o) {
            try {
                o.wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public void dumpStream(InputStream in) throws IOException {
        String tempString;
        int count = in.available();
        while (count > 0) {
            byte[] b = new byte[count];
            in.read(b);
            tempString = new String(b);
            if (tempString.indexOf("Exception") != -1)
                throw new RuntimeException("[RESULT] :" + " Exception in child process : FAIL");
            System.out.println(tempString);
            count = in.available();
        }
    }

    public void lostOwnership(Clipboard clip, Transferable contents) {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            clipboard.getContents(null);
            Thread.currentThread().interrupt();
            clipboard.getContents(null);
            clipboard.setContents(transferable, null);
        }).start();
    }
}
