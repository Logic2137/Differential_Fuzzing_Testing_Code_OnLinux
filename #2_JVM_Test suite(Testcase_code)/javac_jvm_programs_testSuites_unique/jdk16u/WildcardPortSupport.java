




import com.sun.jdi.*;
import com.sun.jdi.connect.*;

import java.io.IOException;
import java.io.PrintStream;

import java.util.Map;

public class WildcardPortSupport {

    private static final String PORT_ARG = "port";

    public static void main(String argv[]) throws Exception {
        WildcardPortSupport test = new WildcardPortSupport();
        test.runAllTests();
    }

    public void runAllTests() throws Exception {
        ListeningConnector connector =
                Bootstrap.virtualMachineManager().listeningConnectors().stream().
                        filter(c -> c.name().equals("com.sun.jdi.SocketListen")).findFirst().get();

        if (connector == null) {
            throw new RuntimeException("FAILURE: no com.sun.jdi.SocketListen connectors found!");
        }

        testWithDefaultArgs1(connector);
        testWithDefaultArgs2(connector);
        testWithWildcardPort1(connector);
        testWithWildcardPort2(connector);
        testWithDefaultArgsNegative(connector);
    }


    
    private void testWithDefaultArgs1(ListeningConnector connector) throws IOException,
            IllegalConnectorArgumentsException {
        int port1 = startListening(connector, connector.defaultArguments());
        int port2 = startListening(connector, connector.defaultArguments());
        connector.stopListening(getArgumentsMap(connector, port1));
        connector.stopListening(getArgumentsMap(connector, port2));
    }

    
    private void testWithDefaultArgs2(ListeningConnector connector) throws IOException,
            IllegalConnectorArgumentsException {
        Map<String, Connector.Argument> args1 = connector.defaultArguments();
        startListening(connector, args1);
        Map<String, Connector.Argument> args2 = connector.defaultArguments();
        startListening(connector, args2);
        connector.stopListening(args1);
        connector.stopListening(args2);
    }

    
    private void testWithWildcardPort1(ListeningConnector connector) throws IOException,
            IllegalConnectorArgumentsException {
        int port1 = startListening(connector, getArgumentsMap(connector, 0));
        int port2 = startListening(connector, getArgumentsMap(connector, 0));
        connector.stopListening(getArgumentsMap(connector, port1));
        connector.stopListening(getArgumentsMap(connector, port2));
    }

    
    private void testWithWildcardPort2(ListeningConnector connector) throws IOException,
            IllegalConnectorArgumentsException {
        Map<String, Connector.Argument> args1 = getArgumentsMap(connector, 0);
        startListening(connector, args1);
        Map<String, Connector.Argument> args2 = getArgumentsMap(connector, 0);
        startListening(connector, args2);
        connector.stopListening(args1);
        connector.stopListening(args2);
    }

    
    private void testWithDefaultArgsNegative(ListeningConnector connector) throws IOException,
            IllegalConnectorArgumentsException {
        Map<String, Connector.Argument> args = connector.defaultArguments();
        connector.startListening(args);
        String port = args.get(PORT_ARG).value();
        if (port.isEmpty() || "0".equals(port)) {
            throw new RuntimeException("Test testWithDefaultArgsNegative failed." +
                    " The argument map was not updated with the bound port number.");
        }
        try {
            
            
            connector.startListening(args);
        } catch (IllegalConnectorArgumentsException ex) {
            System.out.println("Expected exception caught" + ex.getMessage());
            return;
        } finally {
            connector.stopListening(args);
        }
        throw new RuntimeException("Test testWithDefaultArgsNegative failed. No expected " +
                "com.sun.jdi.IllegalConnectorArgumentsException exception was thrown.");
    }

    private int startListening(ListeningConnector connector, Map<String, Connector.Argument> args)
            throws IOException, IllegalConnectorArgumentsException {
        String address = connector.startListening(args);
        return Integer.valueOf(address.split(":")[1]);
    }


    private Map<String, Connector.Argument> getArgumentsMap(ListeningConnector connector, int port) {
        Map<String, Connector.Argument> args = connector.defaultArguments();
        Connector.Argument arg = args.get(PORT_ARG);
        arg.setValue(String.valueOf(port));
        return args;
    }
}
