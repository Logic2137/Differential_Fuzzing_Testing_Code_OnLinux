
package jdk.test.lib.jfr;

import com.sun.tools.attach.VirtualMachine;
import java.nio.file.Path;
import java.nio.file.Paths;



public class StreamingUtils {
    
    public static Path getJfrRepository(Process process) throws Exception {
        while (true) {
            if (!process.isAlive()) {
                String msg = String.format("Process (pid = %d) is no longer alive, exit value = %d",
                                           process.pid(), process.exitValue());
                throw new RuntimeException(msg);
            }

            try {
                VirtualMachine vm = VirtualMachine.attach(String.valueOf(process.pid()));
                String repo = vm.getSystemProperties().getProperty("jdk.jfr.repository");
                if (repo != null) {
                    vm.detach();
                    System.out.println("JFR repository: " + repo);
                    return Paths.get(repo);
                }
            } catch (Exception e) {
                System.out.println("Attach failed: " + e.getMessage());
                System.out.println("Retrying...");
            }
            Thread.sleep(500);
        }
    }

}
