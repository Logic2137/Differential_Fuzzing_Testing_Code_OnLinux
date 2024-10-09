import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertyResolvingWrapper {

    private static final Properties properties;

    static {
        Properties p = System.getProperties();
        String name = p.getProperty("os.name");
        String arch = p.getProperty("os.arch");
        String family;
        String simple_arch;
        if (name.startsWith("AIX"))
            family = "aix";
        else if (name.startsWith("Linux"))
            family = "linux";
        else if (name.startsWith("Mac") || name.startsWith("Darwin"))
            family = "mac";
        else if (name.startsWith("OS400") || name.startsWith("OS/400"))
            family = "os400";
        else if (name.startsWith("SunOS") || name.startsWith("Solaris"))
            family = "solaris";
        else if (name.startsWith("Windows"))
            family = "windows";
        else
            family = name.replaceFirst("^([^ ]+).*", "$1");
        if (arch.contains("64") && !arch.equals("ia64") && !arch.equals("ppc64") && !arch.equals("ppc64le") && !arch.equals("zArch_64") && !arch.equals("aarch64"))
            simple_arch = "x64";
        else if (arch.contains("86"))
            simple_arch = "i586";
        else if (arch.equals("ppc") || arch.equals("powerpc"))
            simple_arch = "ppc";
        else if (arch.equals("s390x") || arch.equals("zArch_64"))
            simple_arch = "s390x";
        else
            simple_arch = arch;
        p.setProperty("os.family", family);
        p.setProperty("os.simpleArch", simple_arch);
        properties = p;
    }

    public static void main(String[] args) throws Throwable {
        List<String> command = new ArrayList<>(args.length);
        for (int i = 0; i < args.length; ++i) {
            StringBuilder arg = new StringBuilder(args[i]);
            while (i < args.length - 1 && (arg.chars().filter(c -> c == '"').count() % 2) != 0) {
                arg.append(" ").append(args[++i]);
            }
            command.add(eval(arg.toString()));
        }
        System.out.println("run " + command);
        try {
            Class.forName(command.remove(0)).getMethod("main", String[].class).invoke(null, new Object[] { command.toArray(new String[0]) });
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            t = t != null ? t : e;
            throw t;
        }
    }

    private static String eval(String string) {
        int index;
        int current = 0;
        StringBuilder result = new StringBuilder();
        while (current < string.length() && (index = string.indexOf("${", current)) >= 0) {
            result.append(string.substring(current, index));
            int endName = string.indexOf('}', index);
            current = endName + 1;
            String name = string.substring(index + 2, endName);
            String value = properties.getProperty(name);
            if (value == null) {
                throw new Error("can't find property " + name);
            }
            result.append(value);
        }
        if (current < string.length()) {
            result.append(string.substring(current));
        }
        int length = result.length();
        if (length > 1 && result.charAt(0) == '"' && result.charAt(length - 1) == '"') {
            result.deleteCharAt(length - 1);
            result.deleteCharAt(0);
        }
        return result.toString();
    }
}
