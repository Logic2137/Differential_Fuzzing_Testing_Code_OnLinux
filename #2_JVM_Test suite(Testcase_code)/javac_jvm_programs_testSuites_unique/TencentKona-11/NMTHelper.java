

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.lang.management.ManagementFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class NMTHelper
{
    public static void baseline() {
        executeDcmd("vmNativeMemory", "baseline");
    }

    
    private static Pattern totalLine = Pattern.compile("^Total: reserved=\\d+KB .*KB, committed=\\d+KB (.*)KB$");

    public static long committedDiff() throws Exception {
        String res = (String) executeDcmd("vmNativeMemory", "detail.diff");
        String[] lines = res.split("\n");
        for (String line : lines) {
            Matcher matcher = totalLine.matcher(line);
            if (matcher.matches()) {
                String committed = matcher.group(1);
                return Long.parseLong(committed);
            }
        }
        throw new Exception("Could not find the Total line in the NMT output.");
    }

    private static String executeDcmd(String cmd, String ... args) {
        ObjectName oname = null;
        try {
            oname = ObjectName.getInstance("com.sun.management:type=DiagnosticCommand");
        } catch (MalformedObjectNameException mone) {
            throw new RuntimeException(mone);
        }
        Object[] dcmdArgs = {args};
        String[] signature = {String[].class.getName()};

        String cmdString = cmd + " " +
            Arrays.stream(args).collect(Collectors.joining(" "));
        File f = new File("dcmdoutput-" + cmd + "-" + System.currentTimeMillis() + ".txt");
        System.out.println("Output from Dcmd '" + cmdString + "' is being written to file " + f);
        try (FileWriter fw = new FileWriter(f)) {
            fw.write("> " + cmdString + ":");
            String result = (String)ManagementFactory.getPlatformMBeanServer().
                    invoke(oname, cmd, dcmdArgs, signature);
            fw.write(result);
            return result;
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
