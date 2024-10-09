

import java.util.List;
import java.util.ArrayList;

public class PrintEnv {

    public static void main(String[] args) {
        List<String> lines = printArgs(args);
        lines.forEach(System.out::println);
    }

    private static List<String> printArgs(String[] args) {
        List<String> lines = new ArrayList<>();

        for (String arg : args) {
            if (arg.startsWith(PRINT_ENV_VAR)) {
                String name = arg.substring(PRINT_ENV_VAR.length());
                lines.add(name + "=" + System.getenv(name));
            } else if (arg.startsWith(PRINT_SYS_PROP)) {
                String name = arg.substring(PRINT_SYS_PROP.length());
                lines.add(name + "=" + System.getProperty(name));
            } else {
                throw new IllegalArgumentException();
            }
        }

        return lines;
    }

    private final static String PRINT_ENV_VAR = "--print-env-var=";
    private final static String PRINT_SYS_PROP = "--print-sys-prop=";
}
