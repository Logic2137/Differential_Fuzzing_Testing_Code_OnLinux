




import java.security.*;

public class SignatureGetAlgorithm {

    public static void main(String[] args) throws Exception {
        Provider testProvider = new TestProvider();
        Security.addProvider(testProvider);
        Signature sig = Signature.getInstance("MySignatureAlg");
        String algorithm = sig.getAlgorithm();
        System.out.println("Algorithm Name: " + algorithm);
        if (algorithm == null) {
            throw new Exception("algorithm name should be 'MySignatureAlg'");
        }
    }

    public static class TestProvider extends Provider {
        TestProvider() {
            super("testSignatureGetAlgorithm", "1.0", "test Signatures");
            put("Signature.MySignatureAlg",
                "SignatureGetAlgorithm$MySignatureAlg");
        }
    }

    public static class MySignatureAlg extends Signature {

        public MySignatureAlg() {
            super(null);
        }

        MySignatureAlg(String s) {
            super(s);
        }

        @Override
        protected void engineInitVerify(PublicKey publicKey)
                throws InvalidKeyException {
        }

        @Override
        protected void engineInitSign(PrivateKey privateKey)
                throws InvalidKeyException {
        }

        @Override
        protected void engineUpdate(byte b) throws SignatureException {
        }

        @Override
        protected void engineUpdate(byte[] b, int off, int len)
                throws SignatureException {
        }

        @Override
        protected byte[] engineSign()
                throws SignatureException {
            return new byte[0];
        }

        @Override
        protected boolean engineVerify(byte[] sigBytes)
                throws SignatureException {
            return false;
        }

        @Override
        @Deprecated
        protected void engineSetParameter(String param, Object value)
                throws InvalidParameterException {
        }

        @Override
        @Deprecated
        protected Object engineGetParameter(String param)
                throws InvalidParameterException {
            return null;
        }
    }
}
