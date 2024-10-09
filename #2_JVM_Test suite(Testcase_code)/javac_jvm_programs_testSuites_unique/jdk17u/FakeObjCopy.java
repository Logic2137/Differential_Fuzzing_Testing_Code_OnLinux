import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FakeObjCopy {

    private static final String OBJCOPY_ONLY_KEEP_DEBUG_OPT = "--only-keep-debug";

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new AssertionError("At least one argument expected");
        }
        String[] objCopyArgs = new String[args.length - 1];
        System.arraycopy(args, 1, objCopyArgs, 0, objCopyArgs.length);
        String logFile = args[0];
        System.out.println("DEBUG: Fake objcopy called. Log file is: " + logFile);
        String line = Arrays.asList(objCopyArgs).stream().collect(Collectors.joining(" "));
        Files.write(Paths.get(logFile), List.<String>of(line), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        if (objCopyArgs.length == 3 && OBJCOPY_ONLY_KEEP_DEBUG_OPT.equals(objCopyArgs[0])) {
            handleOnlyKeepDebug(objCopyArgs[2]);
        }
    }

    private static void handleOnlyKeepDebug(String dbgFile) throws Exception {
        try (PrintWriter pw = new PrintWriter(new File(dbgFile))) {
            pw.println("Fake objcopy debug info file");
        }
        System.out.println("DEBUG: wrote fake debug file " + dbgFile);
    }
}
