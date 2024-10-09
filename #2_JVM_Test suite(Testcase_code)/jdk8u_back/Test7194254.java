import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test7194254 {

    public static void main(String[] args) throws Exception {
        final int NUMBER_OF_JAVA_PRIORITIES = Thread.MAX_PRIORITY - Thread.MIN_PRIORITY + 1;
        final CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_JAVA_PRIORITIES + 1);
        for (int p = Thread.MIN_PRIORITY; p <= Thread.MAX_PRIORITY; ++p) {
            final int priority = p;
            new Thread("Priority=" + p) {

                {
                    setPriority(priority);
                }

                public void run() {
                    try {
                        barrier.await();
                        barrier.await();
                    } catch (Exception exc) {
                    }
                }
            }.start();
        }
        barrier.await();
        int matches = 0;
        List<String> failed = new ArrayList<>();
        try {
            String pid = getPid();
            String jstack = System.getProperty("java.home") + "/../bin/jstack";
            Process process = new ProcessBuilder(jstack, pid).redirectErrorStream(true).start();
            Pattern pattern = Pattern.compile("\\\"Priority=(\\d+)\\\".* prio=(\\d+).*");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        matches += 1;
                        String expected = matcher.group(1);
                        String actual = matcher.group(2);
                        if (!expected.equals(actual)) {
                            failed.add(line);
                        }
                    }
                }
            }
            barrier.await();
        } finally {
            barrier.reset();
        }
        if (matches != NUMBER_OF_JAVA_PRIORITIES) {
            throw new AssertionError("matches: expected " + NUMBER_OF_JAVA_PRIORITIES + ", but was " + matches);
        }
        if (!failed.isEmpty()) {
            throw new AssertionError(failed.size() + ":" + failed);
        }
        System.out.println("Test passes.");
    }

    static String getPid() {
        RuntimeMXBean runtimebean = ManagementFactory.getRuntimeMXBean();
        String vmname = runtimebean.getName();
        int i = vmname.indexOf('@');
        if (i != -1) {
            vmname = vmname.substring(0, i);
        }
        return vmname;
    }
}
