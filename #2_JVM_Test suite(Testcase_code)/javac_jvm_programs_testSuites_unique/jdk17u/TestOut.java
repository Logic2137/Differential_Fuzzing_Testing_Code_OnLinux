
package org.netbeans.jemmy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;

public class TestOut {

    private InputStream input;

    private PrintWriter output;

    private PrintWriter errput;

    private PrintWriter golden_output;

    private BufferedReader buffInput;

    private boolean autoFlushMode = true;

    public TestOut(InputStream in, PrintStream out, PrintStream err) {
        this(in, out, err, null);
    }

    public TestOut(InputStream in, PrintStream out, PrintStream err, PrintStream golden) {
        super();
        PrintWriter tout = null;
        if (out != null) {
            tout = new PrintWriter(out);
        }
        PrintWriter terr = null;
        if (err != null) {
            terr = new PrintWriter(err);
        }
        PrintWriter tgolden = null;
        if (golden != null) {
            tgolden = new PrintWriter(golden);
        }
        initStreams(in, tout, terr, tgolden);
    }

    public TestOut(InputStream in, PrintWriter out, PrintWriter err) {
        this(in, out, err, null);
    }

    public TestOut(InputStream in, PrintWriter out, PrintWriter err, PrintWriter golden) {
        super();
        initStreams(in, out, err, golden);
        autoFlushMode = true;
    }

    public TestOut() {
        this(System.in, new PrintWriter(System.out), new PrintWriter(System.err), null);
    }

    public static TestOut getNullOutput() {
        return new TestOut((InputStream) null, (PrintWriter) null, (PrintWriter) null);
    }

    public boolean setAutoFlushMode(boolean autoFlushMode) {
        boolean oldValue = getAutoFlushMode();
        this.autoFlushMode = autoFlushMode;
        return oldValue;
    }

    public boolean getAutoFlushMode() {
        return autoFlushMode;
    }

    public int read() throws IOException {
        if (input != null) {
            return input.read();
        } else {
            return -1;
        }
    }

    public String readLine() throws IOException {
        if (buffInput != null) {
            return buffInput.readLine();
        } else {
            return null;
        }
    }

    public void print(String line) {
        if (output != null) {
            output.print(line);
        }
    }

    public void printLine(String line) {
        if (output != null) {
            output.println(line);
            if (autoFlushMode) {
                output.flush();
            }
        }
    }

    public void printGolden(String line) {
        if (golden_output != null) {
            golden_output.println(line);
            if (autoFlushMode) {
                golden_output.flush();
            }
        }
    }

    public void printErrLine(String line) {
        if (errput != null) {
            errput.println(line);
            if (autoFlushMode) {
                errput.flush();
            }
        }
    }

    public void printLine(boolean toOut, String line) {
        if (toOut) {
            printLine(line);
        } else {
            printErrLine(line);
        }
    }

    public void printTrace(String text) {
        printLine("Trace:");
        printLine(text);
    }

    public void printError(String text) {
        printErrLine("Error:");
        printErrLine(text);
    }

    public void printStackTrace(Throwable e) {
        if (errput != null) {
            e.printStackTrace(errput);
            if (autoFlushMode) {
                errput.flush();
            }
        }
    }

    public InputStream getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public PrintWriter getErrput() {
        return errput;
    }

    public PrintWriter getGolden() {
        return golden_output;
    }

    public TestOut createErrorOutput() {
        return new TestOut(null, null, getErrput());
    }

    public void flush() {
        if (output != null) {
            output.flush();
        }
        if (errput != null) {
            errput.flush();
        }
        if (golden_output != null) {
            golden_output.flush();
        }
    }

    private void initStreams(InputStream in, PrintWriter out, PrintWriter err, PrintWriter golden) {
        input = in;
        output = out;
        errput = err;
        golden_output = golden;
        if (input != null) {
            buffInput = new BufferedReader(new InputStreamReader(in));
        } else {
            buffInput = null;
        }
    }
}
