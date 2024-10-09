public class Suicide {

    public static void main(String[] args) {
        String cmd = null;
        try {
            long pid = ProcessHandle.current().pid();
            String osName = System.getProperty("os.name");
            if (osName.contains("Windows")) {
                cmd = "taskkill.exe /F /PID " + pid;
            } else {
                cmd = "kill -9 " + pid;
            }
            System.out.printf("executing `%s'%n", cmd);
            Runtime.getRuntime().exec(cmd);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.printf("TEST/ENV BUG: %s didn't kill JVM%n", cmd);
        System.exit(1);
    }
}
