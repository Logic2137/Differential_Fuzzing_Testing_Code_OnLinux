



import java.util.BitSet;

public final class HugeToString {

    public static void main(String[] args) {
        BitSet bs = new BitSet(500_000_000);
        bs.flip(0, 500_000_000);
        try {
            bs.toString();
        } catch (OutOfMemoryError expected) {
        } catch (Throwable t) {
            throw new AssertionError("Unexpected exception", t);
        }
    }
}
