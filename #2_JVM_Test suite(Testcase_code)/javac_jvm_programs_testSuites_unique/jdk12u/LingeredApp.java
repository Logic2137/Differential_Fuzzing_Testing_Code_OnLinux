

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class LingeredApp {

    private static final long spinDelay = 1000;

    private long lockCreationTime;
    private final ArrayList<String> storedAppOutput;

    protected Process appProcess;
    protected static final int appWaitTime = 100;
    protected final String lockFileName;

    
    class InputGobbler extends Thread {

        InputStream is;
        List<String> astr;

        InputGobbler(InputStream is, List<String> astr) {
            this.is = is;
            this.astr = astr;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    astr.add(line);
                }
            } catch (IOException ex) {
                
            }
        }
    }

    
    public LingeredApp(String lockFileName) {
        this.lockFileName = lockFileName;
        this.storedAppOutput = new ArrayList<String>();
    }

    public LingeredApp() {
        final String lockName = UUID.randomUUID().toString() + ".lck";
        this.lockFileName = lockName;
        this.storedAppOutput = new ArrayList<String>();
    }

    
    public String getLockFileName() {
        return this.lockFileName;
    }

    
    public String getAppName() {
        return this.getClass().getName();
    }

    
    public long getPid() {
        if (appProcess == null) {
            throw new RuntimeException("Process is not alive");
        }
        return appProcess.pid();
    }

    
    public Process getProcess() {
        return appProcess;
    }

    
    public List<String> getAppOutput() {
        if (appProcess.isAlive()) {
            throw new RuntimeException("Process is still alive. Can't get its output.");
        }
        return storedAppOutput;
    }

    
    private static long epoch() {
        return new Date().getTime();
    }

    private static long lastModified(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        return attr.lastModifiedTime().toMillis();
    }

    private static void setLastModified(String fileName, long newTime) throws IOException {
        Path path = Paths.get(fileName);
        FileTime fileTime = FileTime.fromMillis(newTime);
        Files.setLastModifiedTime(path, fileTime);
    }

    
    public void createLock() throws IOException {
        Path path = Paths.get(lockFileName);
        
        Files.createFile(path);
        lockCreationTime = lastModified(lockFileName);
    }

    
    public void deleteLock() throws IOException {
        try {
            Path path = Paths.get(lockFileName);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            
        }
    }

    public void waitAppTerminate() {
        while (true) {
            try {
                appProcess.waitFor();
                break;
            } catch (InterruptedException ex) {
                
            }
        }
    }

    
    public void waitAppReady(long timeout) throws IOException {
        long here = epoch();
        while (true) {
            long epoch = epoch();
            if (epoch - here > (timeout * 1000)) {
                throw new IOException("App waiting timeout");
            }

            
            long lm = lastModified(lockFileName);
            if (lm > lockCreationTime) {
                break;
            }

            
            if (!appProcess.isAlive()) {
                throw new IOException("App exited unexpectedly with " + appProcess.exitValue());
            }

            try {
                Thread.sleep(spinDelay);
            } catch (InterruptedException ex) {
                
            }
        }
    }

    
    public List<String> runAppPrepare(List<String> vmArguments) {
        
        
        
        String jdkPath = System.getProperty("test.jdk");
        if (jdkPath == null) {
            
            Map<String, String> env = System.getenv();
            jdkPath = env.get("TESTJAVA");
        }

        if (jdkPath == null) {
            throw new RuntimeException("Can't determine jdk path neither test.jdk property no TESTJAVA env are set");
        }

        String osname = System.getProperty("os.name");
        String javapath = jdkPath + ((osname.startsWith("window")) ? "/bin/java.exe" : "/bin/java");

        List<String> cmd = new ArrayList<String>();
        cmd.add(javapath);


        if (vmArguments == null) {
            
            String testVmOpts[] = System.getProperty("test.vm.opts","").split("\\s+");
            for (String s : testVmOpts) {
                if (!s.equals("")) {
                    cmd.add(s);
                }
            }
        }
        else{
            
            cmd.addAll(vmArguments);
        }

        
        cmd.add("-cp");
        String classpath = System.getProperty("test.class.path");
        cmd.add((classpath == null) ? "." : classpath);

        return cmd;
    }

    
    public void printCommandLine(List<String> cmd) {
        
        StringBuilder cmdLine = new StringBuilder();
        for (String strCmd : cmd) {
            cmdLine.append("'").append(strCmd).append("' ");
        }

        System.out.println("Command line: [" + cmdLine.toString() + "]");
    }

    public void startGobblerPipe() {
      
      InputGobbler gb = new InputGobbler(appProcess.getInputStream(), storedAppOutput);
      gb.start();
    }

    
    public void runApp(List<String> vmArguments)
            throws IOException {

        List<String> cmd = runAppPrepare(vmArguments);

        cmd.add(this.getAppName());
        cmd.add(lockFileName);

        printCommandLine(cmd);

        ProcessBuilder pb = new ProcessBuilder(cmd);
        
        
        
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        appProcess = pb.start();

        startGobblerPipe();
    }

    
    public void stopApp() throws IOException {
        deleteLock();
        
        
        if (appProcess != null) {
            waitAppTerminate();
            int exitcode = appProcess.exitValue();
            if (exitcode != 0) {
                throw new IOException("LingeredApp terminated with non-zero exit code " + exitcode);
            }
        }
    }

    
    
    public static LingeredApp startApp(List<String> cmd) throws IOException {
        LingeredApp a = new LingeredApp();
        a.createLock();
        try {
            a.runApp(cmd);
            a.waitAppReady(appWaitTime);
        } catch (Exception ex) {
            a.deleteLock();
            throw ex;
        }

        return a;
    }

    

    public static void startApp(List<String> cmd, LingeredApp theApp) throws IOException {
        theApp.createLock();
        try {
            theApp.runApp(cmd);
            theApp.waitAppReady(appWaitTime);
        } catch (Exception ex) {
            theApp.deleteLock();
            throw ex;
        }
    }

    public static LingeredApp startApp() throws IOException {
        return startApp(null);
    }

    public static void stopApp(LingeredApp app) throws IOException {
        if (app != null) {
            
            
            app.stopApp();
        }
    }

    

    public static boolean isLastModifiedWorking() {
        boolean sane = true;
        try {
            long lm = lastModified(".");
            if (lm == 0) {
                System.err.println("SANITY Warning! The lastModifiedTime() doesn't work on this system, it returns 0");
                sane = false;
            }

            long now = epoch();
            if (lm > now) {
                System.err.println("SANITY Warning! The Clock is wrong on this system lastModifiedTime() > getTime()");
                sane = false;
            }

            setLastModified(".", epoch());
            long lm1 = lastModified(".");
            if (lm1 <= lm) {
                System.err.println("SANITY Warning! The setLastModified doesn't work on this system");
                sane = false;
            }
        }
        catch(IOException e) {
            System.err.println("SANITY Warning! IOException during sanity check " + e);
            sane = false;
        }

        return sane;
    }

    
    public static void main(String args[]) {

        if (args.length != 1) {
            System.err.println("Lock file name is not specified");
            System.exit(7);
        }

        String theLockFileName = args[0];

        try {
            Path path = Paths.get(theLockFileName);

            while (Files.exists(path)) {
                
                setLastModified(theLockFileName, epoch());
                Thread.sleep(spinDelay);
            }
        } catch (NoSuchFileException ex) {
            
            
        } catch (Exception ex) {
            System.err.println("LingeredApp ERROR: " + ex);
            
            System.exit(3);
        }

        System.exit(0);
    }
}
