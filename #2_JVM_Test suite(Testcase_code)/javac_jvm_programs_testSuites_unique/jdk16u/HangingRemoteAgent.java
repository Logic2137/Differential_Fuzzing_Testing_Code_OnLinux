

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.jshell.JShell;
import jdk.jshell.execution.JdiExecutionControlProvider;
import jdk.jshell.execution.RemoteExecutionControl;
import jdk.jshell.spi.ExecutionControlProvider;


class HangingRemoteAgent extends RemoteExecutionControl {

    private static final long DELAY = 4000L;
    private static final int TIMEOUT = 2000;
    private static final boolean INFRA_VERIFY = false;

    public static void main(String[] args) throws Exception {
        if (INFRA_VERIFY) {
            RemoteExecutionControl.main(args);
        } else {
            long end = System.currentTimeMillis() + DELAY;
            long remaining;
            while ((remaining = end - System.currentTimeMillis()) > 0L) {
                try {
                    Thread.sleep(remaining);
                } catch (InterruptedException ex) {
                    
                }
            }
        }
    }

    static JShell state(boolean isLaunch, String host) {
        ExecutionControlProvider ecp = new JdiExecutionControlProvider();
        Map<String,String> pm = ecp.defaultParameters();
        pm.put(JdiExecutionControlProvider.PARAM_REMOTE_AGENT, HangingRemoteAgent.class.getName());
        pm.put(JdiExecutionControlProvider.PARAM_HOST_NAME, host==null? "" : host);
        pm.put(JdiExecutionControlProvider.PARAM_LAUNCH, ""+isLaunch);
        pm.put(JdiExecutionControlProvider.PARAM_TIMEOUT, ""+TIMEOUT);
        
        Logger.getLogger("jdk.jshell.execution").setLevel(Level.ALL);
        return JShell.builder()
                .executionEngine(ecp, pm)
                .build();
    }

}
