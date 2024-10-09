import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class InheritIOEHandle {

    private static enum APP {

        A, B, C
    }

    private static File stopC = new File("StopC.txt");

    private static String SIGNAL = "After call child process";

    private static String JAVA_EXE = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

    private static String[] getCommandArray(String processName) {
        String[] cmdArray = { JAVA_EXE, "-cp", System.getProperty("java.class.path"), InheritIOEHandle.class.getName(), processName };
        return cmdArray;
    }

    public static void main(String[] args) throws Exception {
        if (!System.getProperty("os.name").startsWith("Windows")) {
            return;
        }
        APP app = (args.length > 0) ? APP.valueOf(args[0]) : APP.A;
        switch(app) {
            case A:
                performA();
                break;
            case B:
                performB();
                break;
            case C:
                performC();
                break;
        }
    }

    private static void performA() {
        try {
            stopC.delete();
            ProcessBuilder builder = new ProcessBuilder(getCommandArray(APP.B.name()));
            builder.redirectErrorStream(true);
            Process process = builder.start();
            process.getOutputStream().close();
            process.getErrorStream().close();
            boolean isSignalReceived = false;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"))) {
                String result;
                while ((result = in.readLine()) != null) {
                    if (SIGNAL.equals(result)) {
                        isSignalReceived = true;
                        break;
                    } else {
                        throw new RuntimeException("Catastrophe in process B! Bad output.");
                    }
                }
            }
            if (!isSignalReceived) {
                throw new RuntimeException("Signal from B was not received");
            }
            System.out.println("Received signal from B, creating file StopC");
            boolean isFileStopC = stopC.createNewFile();
            if (!isFileStopC) {
                throw new RuntimeException("Signal file StopC.txt was not created. TEST or INFRA bug");
            }
            process.waitFor();
            System.err.println("Read stream finished.");
        } catch (IOException ex) {
            throw new RuntimeException("Catastrophe in process A!", ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException("A was interrupted while waiting for B", ex);
        }
    }

    private static void performB() {
        try {
            ProcessBuilder builder = new ProcessBuilder(getCommandArray(APP.C.name()));
            Process process = builder.start();
            process.getInputStream().close();
            process.getOutputStream().close();
            process.getErrorStream().close();
            System.out.println(SIGNAL);
            process.waitFor();
        } catch (IOException ex) {
            throw new RuntimeException("Catastrophe in process B!", ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException("B was interrupted while waiting for C", ex);
        }
    }

    private static void performC() {
        for (int i = 0; i < 5 * 60; ++i) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            if (stopC.exists()) {
                break;
            }
        }
    }
}
