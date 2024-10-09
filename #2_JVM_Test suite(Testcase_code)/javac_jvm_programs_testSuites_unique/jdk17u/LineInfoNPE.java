import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class LineInfoNPE {

    static final int STATUS_PASSED = 0;

    static final int STATUS_FAILED = 2;

    static final int STATUS_TEMP = 95;

    public static void main(String[] argv) throws Exception {
        int testExitStatus = run(argv, System.out);
        if (testExitStatus != STATUS_PASSED) {
            throw new Exception("test FAILED!");
        }
    }

    public static int run(String[] argv, java.io.PrintStream out) {
        int testResult = STATUS_PASSED;
        out.println("\n==> Test for LineEvent class:");
        Line testLine = new TestLine();
        Line nullLine = null;
        LineEvent.Type testLineEventType = LineEvent.Type.OPEN;
        LineEvent.Type nullLineEventType = null;
        LineEvent testedLineEvent = null;
        out.println("\n>> LineEvent constructor for Line = null: ");
        try {
            testedLineEvent = new LineEvent(nullLine, testLineEventType, (long) 1000);
            out.println(">  No any Exception was thrown!");
            out.println(">  testedLineEvent.getType():");
            try {
                Line producedLine = testedLineEvent.getLine();
                out.println(">   PASSED: producedLine = " + producedLine);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
            out.println(">  testedLineEvent.toString():");
            try {
                String producedString = testedLineEvent.toString();
                out.println(">   PASSED: producedString = " + producedString);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
        } catch (IllegalArgumentException illegArgExcept) {
            out.println(">   PASSED: expected IllegalArgumentException was thrown:");
            illegArgExcept.printStackTrace(out);
        } catch (NullPointerException nullPE) {
            out.println(">   PASSED: expected NullPointerException was thrown:");
            nullPE.printStackTrace(out);
        } catch (Throwable thrown) {
            out.println("##  FAILED: unexpected Exception was thrown:");
            thrown.printStackTrace(out);
            testResult = STATUS_FAILED;
        }
        out.println("\n>> LineEvent constructor for LineEvent.Type = null: ");
        try {
            testedLineEvent = new LineEvent(testLine, nullLineEventType, (long) 1000);
            out.println(">  No any Exception was thrown!");
            out.println(">  testedLineEvent.getType():");
            try {
                LineEvent.Type producedType = testedLineEvent.getType();
                out.println(">   PASSED: producedType = " + producedType);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
            out.println(">  testedLineEvent.toString():");
            try {
                String producedString = testedLineEvent.toString();
                out.println(">   PASSED: producedString = " + producedString);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
        } catch (IllegalArgumentException illegArgExcept) {
            out.println(">   PASSED: expected IllegalArgumentException was thrown:");
            illegArgExcept.printStackTrace(out);
        } catch (NullPointerException nullPE) {
            out.println(">   PASSED: expected NullPointerException was thrown:");
            nullPE.printStackTrace(out);
        } catch (Throwable thrown) {
            out.println("##  FAILED: unexpected Exception was thrown:");
            thrown.printStackTrace(out);
            testResult = STATUS_FAILED;
        }
        if (testResult == STATUS_FAILED) {
            out.println("\n==> test FAILED!");
        } else {
            out.println("\n==> test PASSED!");
        }
        return testResult;
    }
}

class TestLine implements Line {

    public void addLineListener(LineListener listener) {
    }

    public void close() {
    }

    public Control getControl(Control.Type control) {
        return null;
    }

    public Control[] getControls() {
        return new Control[0];
    }

    public Line.Info getLineInfo() {
        return null;
    }

    public boolean isOpen() {
        return false;
    }

    public boolean isControlSupported(Control.Type control) {
        return false;
    }

    public void open() {
    }

    public void removeLineListener(LineListener listener) {
    }
}
