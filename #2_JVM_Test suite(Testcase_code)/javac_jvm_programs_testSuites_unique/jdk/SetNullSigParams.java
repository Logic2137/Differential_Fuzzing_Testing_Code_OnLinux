


import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import sun.security.util.SignatureUtil;

public class SetNullSigParams {

    public static void main(String[] args) throws Exception {
        Signature sig = new SpecialSigImpl();
        SignatureUtil.initVerifyWithParam(sig, (PublicKey) null, null);
        SignatureUtil.initSignWithParam(sig, null, null, null);
    }

    
    
    
    
    private static class SpecialSigImpl extends Signature {
        SpecialSigImpl() {
            super("ANY");
        }
        @Override
        protected void engineInitVerify(PublicKey publicKey)
                throws InvalidKeyException {}
        @Override
        protected void engineInitSign(PrivateKey privateKey)
                throws InvalidKeyException {}
        @Override
        protected void engineUpdate(byte b) throws SignatureException {}
        @Override
        protected void engineUpdate(byte[] b, int off, int len)
                throws SignatureException {}
        @Override
        protected byte[] engineSign() throws SignatureException { return null; }
        @Override
        protected boolean engineVerify(byte[] sigBytes)
                throws SignatureException { return false; }
        @Override
        protected void engineSetParameter(String param, Object value)
                throws InvalidParameterException {}
        @Override
        protected void engineSetParameter(AlgorithmParameterSpec params)
                throws InvalidAlgorithmParameterException {
            if (params == null) throw new NullPointerException("Test Failed");
        }
        @Override
        protected Object engineGetParameter(String param)
                throws InvalidParameterException { return null; }
    }
}
