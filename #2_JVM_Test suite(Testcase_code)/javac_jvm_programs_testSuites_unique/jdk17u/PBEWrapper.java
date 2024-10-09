import java.io.PrintStream;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public abstract class PBEWrapper {

    protected final static int ITERATION_COUNT = 1000;

    protected final SecretKey key;

    protected final Cipher ci;

    protected final String algo;

    protected final PrintStream out;

    public PBEWrapper(String pAlgo, SecretKey pKey, Cipher pCi, PrintStream pOut) {
        this.algo = pAlgo;
        this.key = pKey;
        this.ci = pCi;
        this.out = pOut;
    }

    public abstract boolean execute(int edMode, byte[] inputText, int offset, int len);

    protected static byte[] generateSalt(int numberOfBytes) {
        byte[] salt = new byte[numberOfBytes];
        new Random().nextBytes(salt);
        return salt;
    }

    protected boolean equalsBlock(byte[] b1, int off1, byte[] b2, int off2, int len) {
        for (int i = off1, j = off2, k = 0; k < len; i++, j++, k++) {
            if (b1[i] != b2[j]) {
                return false;
            }
        }
        return true;
    }
}
