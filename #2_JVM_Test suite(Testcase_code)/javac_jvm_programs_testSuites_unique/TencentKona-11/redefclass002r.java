

package nsk.jvmti.RedefineClasses;

import java.io.*;


public class redefclass002r {
    redefclass002r() {
        System.err.println("NEW redefclass002r (" + this +
            "): initializer of the redefined class was invoked!");
    }

    public int checkIt(PrintStream out, boolean verbose) {
        if (verbose)
            out.println("NEW redefclass002r (" + this +
                "): inside the checkIt()");
        return 73;
    }

    public void activeMethod(PipedInputStream pipeIn, PipedOutputStream pipeOut,
            PrintStream out, Object readyObj, boolean verbose) {
        PipedInputStream pIn;
        PipedOutputStream pOut;

        out.println("NEW redefclass002r (" + this +
            "): active frame was replaced!");
        try {
            pOut = new PipedOutputStream(pipeIn);
            pIn = new PipedInputStream(pipeOut);
        } catch (IOException e) {
            out.println("NEW redefclass002r (" + this +
            "): creating a pipe: caught " + e);
            return;
        }

        try {
            pOut.write(230);
            pOut.flush();
            pIn.close();
            pOut.close();
        } catch (IOException e) {
            out.println("NEW redefclass002r (" + this +
            "): caught " + e);
            return;
        }
        if (verbose)
            out.println("NEW redefclass002r (" + this +
                "): exiting...");
    }
}
