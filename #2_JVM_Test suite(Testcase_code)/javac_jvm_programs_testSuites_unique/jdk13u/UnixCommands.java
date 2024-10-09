import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UnixCommands {

    public static final boolean isUnix = !System.getProperty("os.name").startsWith("Windows");

    public static final boolean isLinux = System.getProperty("os.name").startsWith("Linux");

    public static final boolean isSunOS = System.getProperty("os.name").equals("SunOS");

    private static final String[] paths = { "/bin", "/usr/bin" };

    private static Map<String, String> nameToCommand = new HashMap<>(16);

    public static void ensureCommandsAvailable(String... commands) {
        for (String command : commands) {
            if (findCommand(command) == null) {
                throw new Error("Command '" + command + "' not found; bailing out");
            }
        }
    }

    public static String cat() {
        return findCommand("cat");
    }

    public static String sh() {
        return findCommand("sh");
    }

    public static String kill() {
        return findCommand("kill");
    }

    public static String sleep() {
        return findCommand("sleep");
    }

    public static String tee() {
        return findCommand("tee");
    }

    public static String echo() {
        return findCommand("echo");
    }

    public static String findCommand(String name) {
        if (nameToCommand.containsKey(name)) {
            return nameToCommand.get(name);
        }
        String command = findCommand0(name);
        nameToCommand.put(name, command);
        return command;
    }

    private static String findCommand0(String name) {
        for (String path : paths) {
            File file = new File(path, name);
            if (file.canExecute()) {
                return file.getPath();
            }
        }
        return null;
    }
}
