import java.security.*;

public class NoProvider {

    private static class NoProviderPublicKey implements PublicKey {

        public String getAlgorithm() {
            return "NoProvider";
        }

        public String getFormat() {
            return "none";
        }

        public byte[] getEncoded() {
            return new byte[1];
        }
    }

    private static class NoProviderSignature extends Signature {

        public NoProviderSignature() {
            super("NoProvider");
        }

        protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        }

        protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        }

        protected void engineUpdate(byte b) throws SignatureException {
        }

        protected void engineUpdate(byte[] b, int off, int len) throws SignatureException {
        }

        protected byte[] engineSign() throws SignatureException {
            return new byte[1];
        }

        protected boolean engineVerify(byte[] sigBytes) throws SignatureException {
            return false;
        }

        @Deprecated
        protected void engineSetParameter(String param, Object value) throws InvalidParameterException {
        }

        @Deprecated
        protected Object engineGetParameter(String param) throws InvalidParameterException {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        new NoProviderSignature().initVerify(new NoProviderPublicKey());
    }
}
