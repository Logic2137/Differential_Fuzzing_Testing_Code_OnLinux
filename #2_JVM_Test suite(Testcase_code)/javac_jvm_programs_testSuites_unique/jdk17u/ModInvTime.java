import java.math.BigInteger;

public class ModInvTime {

    public static void main(String[] args) throws InterruptedException {
        BigInteger prime = new BigInteger("39402006196394479212279040100143613805079739270465446667946905279627659399113263569398956308152294913554433653942643");
        BigInteger s = new BigInteger("9552729729729327851382626410162104591956625415831952158766936536163093322096473638446154604799898109762512409920799");
        System.out.format("int length: %d, modulus length: %d%n", s.bitLength(), prime.bitLength());
        System.out.println("Computing modular inverse ...");
        BigInteger mi = s.modInverse(prime);
        System.out.format("Modular inverse: %s%n", mi);
        check(s, prime, mi);
        BigInteger ns = s.negate();
        BigInteger nmi = ns.modInverse(prime);
        System.out.format("Modular inverse of negation: %s%n", nmi);
        check(ns, prime, nmi);
    }

    public static void check(BigInteger val, BigInteger mod, BigInteger inv) {
        BigInteger r = inv.multiply(val).remainder(mod);
        if (r.signum() == -1)
            r = r.add(mod);
        if (!r.equals(BigInteger.ONE))
            throw new RuntimeException("Numerically incorrect modular inverse");
    }
}
