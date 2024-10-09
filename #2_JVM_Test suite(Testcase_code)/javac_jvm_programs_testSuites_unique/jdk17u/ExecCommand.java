import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.AccessControlException;

public class ExecCommand {

    static class SecurityMan extends SecurityManager {

        public static String unquote(String str) {
            int length = (str == null) ? 0 : str.length();
            if (length > 1 && str.charAt(0) == '\"' && str.charAt(length - 1) == '\"') {
                return str.substring(1, length - 1);
            }
            return str;
        }

        @Override
        public void checkExec(String cmd) {
            String ncmd = (new File(unquote(cmd))).getPath();
            if (ncmd.equals(".\\Program") || ncmd.equals("\".\\Program") || ncmd.equals(".\\Program Files\\do.cmd") || ncmd.equals(".\\Program.cmd") || ncmd.equals("cmd")) {
                return;
            }
            super.checkExec(cmd);
        }

        @Override
        public void checkDelete(String file) {
        }

        @Override
        public void checkRead(String file) {
        }
    }

    private static final String[] TEST_RTE_ARG = { "cmd /C dir > dirOut.txt", "cmd /C dir > \".\\Program Files\\dirOut.txt\"", ".\\Program Files\\do.cmd", "\".\\Program Files\\doNot.cmd\" arg", "\".\\Program Files\\do.cmd\" arg", "\".\\Program.cmd\" arg", ".\\Program.cmd arg" };

    private static final String[] doCmdCopy = { ".\\Program.cmd", ".\\Program Files\\doNot.cmd", ".\\Program Files\\do.cmd" };

    private static final String[][] TEST_RTE_GI = { new String[] { "Success", "IOException", "Success", "IOException" }, new String[] { "Success", "IOException", "Success", "IOException" }, new String[] { "Success", "IOException", "Success", "IOException" }, new String[] { "Success", "Success", "Success", "AccessControlException" }, new String[] { "Success", "Success", "Success", "Success" }, new String[] { "Success", "Success", "Success", "Success" }, new String[] { "Success", "Success", "Success", "Success" } };

    private static void deleteOut(String path) {
        try {
            Files.delete(FileSystems.getDefault().getPath(path));
        } catch (IOException ex) {
        }
    }

    private static void checkOut(String path) throws FileNotFoundException {
        if (Files.notExists(FileSystems.getDefault().getPath(path)))
            throw new FileNotFoundException(path);
    }

    public static void main(String[] _args) throws Exception {
        if (!System.getProperty("os.name").startsWith("Windows")) {
            return;
        }
        try {
            new File(".\\Program Files").mkdirs();
            for (int i = 0; i < doCmdCopy.length; ++i) {
                try (BufferedWriter outCmd = new BufferedWriter(new FileWriter(doCmdCopy[i]))) {
                    outCmd.write("@echo %1");
                }
            }
        } catch (IOException e) {
            throw new Error(e.getMessage());
        }
        for (int k = 0; k < 4; ++k) {
            switch(k) {
                case 0:
                    break;
                case 1:
                    System.setProperty("jdk.lang.Process.allowAmbiguousCommands", "false");
                    break;
                case 2:
                    System.setProperty("jdk.lang.Process.allowAmbiguousCommands", "");
                    break;
                case 3:
                    System.setSecurityManager(new SecurityMan());
                    break;
            }
            for (int i = 0; i < TEST_RTE_ARG.length; ++i) {
                String outRes;
                try {
                    switch(i) {
                        case 0:
                            deleteOut(".\\dirOut.txt");
                            break;
                        case 1:
                            deleteOut(".\\Program Files\\dirOut.txt");
                            break;
                    }
                    Process exec = Runtime.getRuntime().exec(TEST_RTE_ARG[i]);
                    exec.waitFor();
                    switch(i) {
                        case 0:
                            checkOut(".\\dirOut.txt");
                            break;
                        case 1:
                            checkOut(".\\Program Files\\dirOut.txt");
                            break;
                    }
                    outRes = "Success";
                } catch (IOException ioe) {
                    outRes = "IOException: " + ioe.getMessage();
                } catch (IllegalArgumentException iae) {
                    outRes = "IllegalArgumentException: " + iae.getMessage();
                } catch (AccessControlException se) {
                    outRes = "AccessControlException: " + se.getMessage();
                }
                if (!outRes.startsWith(TEST_RTE_GI[i][k])) {
                    throw new Error("Unexpected output! Step" + k + ":" + i + "\nArgument: " + TEST_RTE_ARG[i] + "\nExpected: " + TEST_RTE_GI[i][k] + "\n  Output: " + outRes);
                } else {
                    System.out.println("RTE OK:" + TEST_RTE_ARG[i]);
                }
            }
        }
    }
}
