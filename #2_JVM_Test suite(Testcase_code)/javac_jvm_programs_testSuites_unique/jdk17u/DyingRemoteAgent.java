import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.jshell.JShell;
import jdk.jshell.execution.JdiExecutionControlProvider;
import jdk.jshell.execution.RemoteExecutionControl;
import jdk.jshell.spi.ExecutionControlProvider;

class DyingRemoteAgent extends RemoteExecutionControl {

    static final boolean INFRA_VERIFY = false;

    public static void main(String[] args) throws Exception {
        if (INFRA_VERIFY) {
            RemoteExecutionControl.main(args);
        } else {
            System.exit(1);
        }
    }

    static JShell state(boolean isLaunch, String host) {
        ExecutionControlProvider ecp = new JdiExecutionControlProvider();
        Map<String, String> pm = ecp.defaultParameters();
        pm.put(JdiExecutionControlProvider.PARAM_REMOTE_AGENT, DyingRemoteAgent.class.getName());
        pm.put(JdiExecutionControlProvider.PARAM_HOST_NAME, host == null ? "" : host);
        pm.put(JdiExecutionControlProvider.PARAM_LAUNCH, "" + isLaunch);
        Logger.getLogger("jdk.jshell.execution").setLevel(Level.ALL);
        return JShell.builder().executionEngine(ecp, pm).build();
    }
}
