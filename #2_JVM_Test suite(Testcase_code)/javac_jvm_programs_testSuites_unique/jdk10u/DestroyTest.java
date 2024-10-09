



import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

abstract class ProcessTest implements Runnable {
    ProcessBuilder bldr;
    Process p;

    public Process killProc(boolean force) throws Exception {
        if (force) {
            p.destroyForcibly();
        } else {
            p.destroy();
        }
        return p;
    }

    public boolean isAlive() {
        return p.isAlive();
    }

    public void run() {
        try {
            String line;
            BufferedReader is =
                new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = is.readLine()) != null)
                System.err.println("ProcessTrap: " + line);
        } catch(IOException e) {
            if (!e.getMessage().matches("[Ss]tream [Cc]losed")) {
                throw new RuntimeException(e);
            }
        }
    }

    public abstract void runTest() throws Exception;
}

class UnixTest extends ProcessTest {
    public UnixTest(File script) throws IOException {
        script.deleteOnExit();
        createScript(script);
        bldr = new ProcessBuilder(script.getCanonicalPath());
        bldr.redirectErrorStream(true);
        bldr.directory(new File("."));
        p = bldr.start();
    }

    void createScript(File processTrapScript) throws IOException {
        processTrapScript.deleteOnExit();
        FileWriter fstream = new FileWriter(processTrapScript);
        try (BufferedWriter out = new BufferedWriter(fstream)) {
            out.write("#!/bin/bash\n" +
                "echo \\\"ProcessTrap.sh started: trapping SIGTERM/SIGINT\\\"\n" +
                "trap bashtrap SIGTERM SIGINT\n" +
                "bashtrap()\n" +
                "{\n" +
                "    echo \\\"SIGTERM/SIGINT detected!\\\"\n" +
                "}\n" +
                "\n" +
                "while :\n" +
                "do\n" +
                "    sleep 1;\n" +
                "done\n");
        }
        processTrapScript.setExecutable(true, true);
    }

    @Override
    public void runTest() throws Exception {
        killProc(false);
        Thread.sleep(1000);
        if (!p.isAlive())
            throw new RuntimeException("Process terminated prematurely.");
        killProc(true).waitFor();
        if (p.isAlive())
            throw new RuntimeException("Problem terminating the process.");
    }
}

class MacTest extends UnixTest {
    public MacTest(File script) throws IOException {
        super(script);
    }

    @Override
    public void runTest() throws Exception {
        
        
        
        
        killProc(true).waitFor();
        if (p.isAlive())
            throw new RuntimeException("Problem terminating the process.");
    }
}

class WindowsTest extends ProcessTest {
    public WindowsTest() throws IOException {
        bldr = new ProcessBuilder("ftp");
        bldr.redirectErrorStream(true);
        bldr.directory(new File("."));
        p = bldr.start();
    }

    @Override
    public void runTest() throws Exception {
        killProc(true).waitFor();
    }
}

public class DestroyTest {

    public static ProcessTest getTest() throws Exception {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            return new WindowsTest();
        } else if (osName.startsWith("Linux") == true) {
            return new UnixTest(
                File.createTempFile("ProcessTrap-", ".sh",null));
        } else if (osName.startsWith("Mac OS")) {
            return new MacTest(
                File.createTempFile("ProcessTrap-", ".sh",null));
        } else if (osName.equals("SunOS")) {
            return new UnixTest(
                File.createTempFile("ProcessTrap-", ".sh",null));
        } else if (osName.equals("AIX")) {
            return new UnixTest(
                File.createTempFile("ProcessTrap-", ".sh",null));
        }
        return null;
    }

    public static void main(String args[]) throws Exception {
        ProcessTest test = getTest();
        if (test == null) {
            throw new RuntimeException("Unrecognised OS");
        } else {
            new Thread(test).start();
            Thread.sleep(1000);
            test.runTest();
        }
    }
}

