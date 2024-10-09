


import java.util.BitSet;

public final class And {
    public static void main(String[] args) throws Exception {
        BitSet a = new BitSet();
        BitSet b = new BitSet();

        a.set(0);
        a.set(70);
        b.set(40);
        a.and(b);
        if (a.length() != 0)
            throw new RuntimeException("Incorrect length after and().");
    }
}
