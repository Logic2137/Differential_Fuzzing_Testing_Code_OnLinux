import java.io.PrintStream;
import java.security.SecureRandom;

class TestThread extends Thread {

    protected String[] basicCipherSuites;

    protected SecureRandom prng;

    protected int iterations = -1;

    protected boolean doRenegotiate;

    protected boolean initiateHandshake;

    protected boolean listenHandshake;

    protected boolean reverseRole;

    protected int verbosity = 0;

    protected PrintStream out = System.out;

    TestThread(String s) {
        super(s);
    }

    public void setBasicCipherSuites(String[] suites) {
        basicCipherSuites = suites;
    }

    public void setListenHandshake(boolean flag) {
        listenHandshake = flag;
    }

    public void setDoRenegotiate(boolean flag) {
        doRenegotiate = flag;
    }

    public void setInitiateHandshake(boolean flag) {
        initiateHandshake = flag;
    }

    public void setReverseRole(boolean flag) {
        reverseRole = flag;
    }

    public void setOutput(PrintStream out) {
        this.out = out;
    }

    public void setVerbosity(int level) {
        verbosity = level;
    }

    public void setIterations(int level) {
        iterations = level;
    }

    void setPRNG(SecureRandom prng) {
        this.prng = prng;
    }
}
