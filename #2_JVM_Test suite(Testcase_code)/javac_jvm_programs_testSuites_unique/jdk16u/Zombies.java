



import java.io.*;

public class Zombies {

    static final String os = System.getProperty("os.name");

    static final String TrueCommand = os.contains("OS X")?
        "/usr/bin/true" : "/bin/true";

    public static void main(String[] args) throws Throwable {
        if (! new File("/usr/bin/perl").canExecute() ||
            ! new File("/bin/ps").canExecute())
            return;
        System.out.println("Looks like a Unix system.");
        long mypid = ProcessHandle.current().pid();
        System.out.printf("mypid: %d%n", mypid);

        final Runtime rt = Runtime.getRuntime();

        try {
            rt.exec("no-such-file");
            throw new Error("expected IOException not thrown");
        } catch (IOException expected) {}

        try {
            rt.exec(".");
            throw new Error("expected IOException not thrown");
        } catch (IOException expected) {}

        try {
            rt.exec(TrueCommand, null, new File("no-such-dir"));
            throw new Error("expected IOException not thrown");
        } catch (IOException expected) {}

        Process p = rt.exec(TrueCommand);
        ProcessHandle pp = p.toHandle().parent().orElse(null);
        System.out.printf("%s pid: %d, parent: %s%n", TrueCommand, p.pid(), pp);
        p.waitFor();

        
        final String[] zombieCounter = {
            "/usr/bin/perl", "-e",
                "$a=`/bin/ps -eo ppid,pid,s,command`;" +
                        "print @b=$a=~/^ *@{[getppid]} +[0-9]+ +Z.*$/mog;" +
                        "exit @b"
        };

        ProcessBuilder pb = new ProcessBuilder(zombieCounter);
        pb.inheritIO();
        int zombies = pb.start().waitFor();
        if (zombies != 0) {
            throw new Error(zombies + " zombies!");
        }
    }
}
