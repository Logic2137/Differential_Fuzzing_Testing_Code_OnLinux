import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import jdk.jshell.execution.DirectExecutionControl;
import jdk.jshell.spi.ExecutionControl;
import jdk.jshell.spi.ExecutionControl.EngineTerminationException;
import jdk.jshell.spi.ExecutionControl.InternalException;
import jdk.jshell.spi.ExecutionControl.RunException;
import static jdk.jshell.execution.Util.forwardExecutionControlAndIO;

public class MyRemoteExecutionControl extends DirectExecutionControl implements ExecutionControl {

    static PrintStream auxPrint;

    public static void main(String[] args) throws Exception {
        try {
            String loopBack = null;
            Socket socket = new Socket(loopBack, Integer.parseInt(args[0]));
            InputStream inStream = socket.getInputStream();
            OutputStream outStream = socket.getOutputStream();
            Map<String, Consumer<OutputStream>> outputs = new HashMap<>();
            outputs.put("out", st -> System.setOut(new PrintStream(st, true)));
            outputs.put("err", st -> System.setErr(new PrintStream(st, true)));
            outputs.put("aux", st -> {
                auxPrint = new PrintStream(st, true);
            });
            Map<String, Consumer<InputStream>> input = new HashMap<>();
            input.put("in", st -> System.setIn(st));
            forwardExecutionControlAndIO(new MyRemoteExecutionControl(), inStream, outStream, outputs, input);
        } catch (Throwable ex) {
            throw ex;
        }
    }

    @Override
    public String varValue(String className, String varName) throws RunException, EngineTerminationException, InternalException {
        auxPrint.print(varName);
        return super.varValue(className, varName);
    }

    @Override
    public Object extensionCommand(String className, Object arg) throws RunException, EngineTerminationException, InternalException {
        if (!arg.equals("test")) {
            throw new InternalException("expected extensionCommand arg to be 'test' got: " + arg);
        }
        return "ribbit";
    }
}
