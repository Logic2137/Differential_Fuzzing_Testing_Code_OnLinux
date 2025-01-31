


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.awt.shell.ShellFolder;

public class ShellFolderMemoryLeak {

    private final static String tempDir = System.getProperty("java.io.tmpdir");
    private static Process process;
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            boolean testResultParallel
                    = createChildProcessWithParallelCollector();
            String result = "";
            if (!testResultParallel) {
                result = "Test failed with Parallel collector";
            }
            boolean testResultDefault
                    = createChildProcessWithDefaultCollector();
            if (!testResultDefault && !testResultParallel) {
                result += " and with default collector both.";
            } else if (!testResultDefault) {
                result = "Test failed with default collector";
            }
            if (!"".equals(result)) {
                throw new RuntimeException(result);
            }
        } else {
            testListFile(args[args.length - 1]);
        }
    }

    public static boolean createChildProcessWithDefaultCollector()
            throws Exception {
        String testDirectory = "TestDirectory1";
        testDirectory = tempDir + testDirectory +File.separator;
        createTestData(testDirectory);
        return runProcess("", testDirectory);
    }

    public static boolean createChildProcessWithParallelCollector()
            throws Exception {
        String testDirectory = "TestDirectory2";
        testDirectory = tempDir + testDirectory +File.separator;
        createTestData(testDirectory);
        return runProcess(" -XX:+UseParallelGC", testDirectory);
    }

    public static boolean runProcess(String arg1, String arg2) throws Exception {
        String javaPath = System.getProperty("java.home");
        String classPathDir = System.getProperty("java.class.path");

        
        String command = javaPath + File.separator + "bin" + File.separator
                + "java -Xmx256M" + arg1 + " -cp "
                + classPathDir
                + " --add-exports=java.desktop/sun.awt.shell=ALL-UNNAMED"
                + " ShellFolderMemoryLeak " + arg2;
        process = Runtime.getRuntime().exec(command);
        BufferedReader input = null;
        InputStream errorStream = null;
        String line = null;
        try {
            int exitVal = process.waitFor();
            input = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            while ((line = input.readLine()) != null) {
            }
            errorStream = process.getErrorStream();
            if (checkExceptions(errorStream) || exitVal != 0) {
                return false;
            }
        } catch (IllegalThreadStateException e) {
            throw new RuntimeException(e);
        } finally {
            if (input != null) {
                input.close();
            }
            if (errorStream != null) {
                errorStream.close();
            }
            process.destroy();
        }
        return true;
    }

    public static boolean checkExceptions(InputStream in) throws IOException {
        String tempString;
        int count = in.available();
        boolean exception = false;
        while (count > 0) {
            byte[] b = new byte[count];
            in.read(b);
            tempString = new String(b);
            if (!exception) {
                exception = tempString.contains("RunTimeException");
            }
            count = in.available();
        }
        return exception;
    }

    private static void createTestData(String testDirectory) {
        String folder = "folder_";
        File testFolder = new File(testDirectory);
        if (testFolder.exists()) {
            clearTestData(testDirectory);
        } else {
            if (testFolder.mkdir()) {
                for (int inx = 0; inx < 100; inx++) {
                    new File(testFolder + File.separator + folder + inx).mkdir();
                }
            } else {
                throw new RuntimeException("Failed to create testDirectory");
            }
        }
    }

    public static void deleteDirectory(File file)
            throws IOException {

        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
            } else {
                String files[] = file.list();
                for (String temp : files) {
                    File fileDelete = new File(file, temp);
                    deleteDirectory(fileDelete);
                }
                if (file.list().length == 0) {
                    file.delete();
                }
            }
        }
    }

    private static void testListFile(String testDirectory) {
        try {
            int mb = 1024 * 1024;
            ShellFolder folder = ShellFolder.getShellFolder(
                    new File(testDirectory));
            Runtime instance = Runtime.getRuntime();

            
            long startmem = instance.totalMemory() - instance.freeMemory();
            long start = System.currentTimeMillis();
            long endmem = 0;

            
            while ((System.currentTimeMillis() - start) < 300000) {
                try {
                    folder.listFiles();
                    Thread.sleep(10);
                    endmem = instance.totalMemory() - instance.freeMemory();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ShellFolderMemoryLeak.class.getName())
                            .log(Level.SEVERE, "InterruptedException", ex);
                }
            }

            
            long result = (endmem - startmem) / mb;

            if (result > 100) {
                clearTestData(testDirectory);
                throw new RuntimeException("Test Failed");
            }
            clearTestData(testDirectory);
        } catch (FileNotFoundException ex) {
            if(process != null && process.isAlive()) {
                process.destroy();
            }
            Logger.getLogger(ShellFolderMemoryLeak.class.getName())
                    .log(Level.SEVERE, "File Not Found Exception", ex);
        }
    }

    private static void clearTestData(String testDirectory) {
        File testFolder = new File(testDirectory);
        try {
            deleteDirectory(testFolder);
        } catch (IOException ex) {
            Logger.getLogger(ShellFolderMemoryLeak.class.getName())
                    .log(Level.SEVERE, "Unable to delete files", ex);
        }
    }
}
