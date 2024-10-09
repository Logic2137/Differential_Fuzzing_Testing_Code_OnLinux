



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

abstract class ProcessTest implements Runnable {
    ProcessBuilder bldr;
    Process p;

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

    public void runTest() throws Exception {
        
        
        
        
        
        p.destroyForcibly();
        p.waitFor();
        if (p.isAlive())
            throw new RuntimeException("Problem terminating the process.");
    }
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
        try (FileWriter fstream = new FileWriter(processTrapScript);
             BufferedWriter out = new BufferedWriter(fstream)) {
            out.write("#!/bin/bash\n" +
                "echo \\\"ProcessTrap.sh started\\\"\n" +
                "while :\n" +
                "do\n" +
                "    sleep 1;\n" +
                "done\n");
        }
        processTrapScript.setExecutable(true, true);
    }

}

class WindowsTest extends ProcessTest {
    public WindowsTest() throws IOException {
        bldr = new ProcessBuilder("ftp");
        bldr.redirectErrorStream(true);
        bldr.directory(new File("."));
        p = bldr.start();
    }

}

public class DestroyTest {

    public static ProcessTest getTest() throws Exception {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            return new WindowsTest();
        } else {
            File userDir = new File(System.getProperty("user.dir", "."));
            File tempFile = File.createTempFile("ProcessTrap-", ".sh", userDir);
            if (osName.startsWith("Linux")
                    || osName.startsWith("Mac OS")
                    || osName.equals("AIX")) {
                return new UnixTest(tempFile);
            }
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

