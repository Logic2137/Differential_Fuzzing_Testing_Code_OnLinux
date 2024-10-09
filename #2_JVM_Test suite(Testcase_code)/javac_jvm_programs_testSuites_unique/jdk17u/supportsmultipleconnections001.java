
package nsk.jdi.ListeningConnector.supportsMultipleConnections;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.ListeningConnector;

public class supportsmultipleconnections001 {

    public static void main(String[] argv) {
        System.exit(run(argv, System.out) + 95);
    }

    public static int run(String[] argv, PrintStream out) {
        int exitCode = 1;
        int exitCode0 = 0;
        String sErr1 = "WARNING\n" + "Method tested: " + "jdi.ListeningConnector.supportsMultipleConnections()\n" + "no ListeningConnector supporting multiconnections\n";
        VirtualMachineManager vmm = Bootstrap.virtualMachineManager();
        List connectorsList = vmm.allConnectors();
        Iterator connectorsListIterator = connectorsList.iterator();
        for (; ; ) {
            try {
                ListeningConnector connector = (ListeningConnector) connectorsListIterator.next();
                if (connector.supportsMultipleConnections()) {
                    exitCode = exitCode0;
                }
            } catch (ClassCastException e) {
            } catch (NoSuchElementException e) {
                break;
            }
        }
        if (exitCode != exitCode0) {
            out.println(sErr1);
        }
        return exitCode0;
    }
}
