import java.security.*;

class DigestBase extends MessageDigestSpi {

    private MessageDigest digest = null;

    public DigestBase(String alg, String provider) throws Exception {
        digest = MessageDigest.getInstance(alg, provider);
    }

    @Override
    protected void engineUpdate(byte input) {
        digest.update(input);
    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len) {
        digest.update(input, offset, len);
    }

    @Override
    protected byte[] engineDigest() {
        return digest.digest();
    }

    @Override
    protected void engineReset() {
        digest.reset();
    }

    public static final class MD5 extends DigestBase {

        public MD5() throws Exception {
            super("MD5", "SUN");
        }
    }

    public static final class SHA extends DigestBase {

        public SHA() throws Exception {
            super("SHA", "SUN");
        }
    }

    public static final class SHA256 extends DigestBase {

        public SHA256() throws Exception {
            super("SHA-256", "SUN");
        }
    }
}
