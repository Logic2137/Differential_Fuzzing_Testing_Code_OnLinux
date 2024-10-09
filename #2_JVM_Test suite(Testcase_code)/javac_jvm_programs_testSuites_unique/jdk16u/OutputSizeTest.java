



import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.ChaCha20ParameterSpec;
import javax.crypto.spec.IvParameterSpec;

public class OutputSizeTest {

    private static final SecureRandom SR = new SecureRandom();

    public static void main(String args[]) throws Exception {
        testCC20GetOutSize();
        testCC20P1305GetOutSize();
        testMultiPartAEADDec();
    }

    private static void testCC20GetOutSize()
            throws GeneralSecurityException {
        boolean result = true;
        KeyGenerator kg = KeyGenerator.getInstance("ChaCha20", "SunJCE");
        kg.init(256);

        
        Cipher cc20 = Cipher.getInstance("ChaCha20", "SunJCE");
        cc20.init(Cipher.ENCRYPT_MODE, kg.generateKey(),
                new ChaCha20ParameterSpec(getRandBuf(12), 10));

        testOutLen(cc20, 0, 0);
        testOutLen(cc20, 5, 5);
        testOutLen(cc20, 5120, 5120);
        
        byte[] input = new byte[5120];
        SR.nextBytes(input);
        cc20.update(input);
        testOutLen(cc20, 1024, 1024);

        
        cc20.init(Cipher.DECRYPT_MODE, kg.generateKey(),
                new ChaCha20ParameterSpec(getRandBuf(12), 10));
        testOutLen(cc20, 0, 0);
        testOutLen(cc20, 5, 5);
        testOutLen(cc20, 5120, 5120);
        
        cc20.update(input);
        testOutLen(cc20, 1024, 1024);
    }

    private static void testCC20P1305GetOutSize()
            throws GeneralSecurityException {
        KeyGenerator kg = KeyGenerator.getInstance("ChaCha20", "SunJCE");
        kg.init(256);

        
        Cipher cc20 = Cipher.getInstance("ChaCha20-Poly1305", "SunJCE");
        cc20.init(Cipher.ENCRYPT_MODE, kg.generateKey(),
                new IvParameterSpec(getRandBuf(12)));

        
        
        testOutLen(cc20, 0, 16);
        testOutLen(cc20, 5, 21);
        testOutLen(cc20, 5120, 5136);
        
        byte[] input = new byte[5120];
        SR.nextBytes(input);
        cc20.update(input);
        testOutLen(cc20, 1024, 1040);

        
        
        
        
        
        cc20.init(Cipher.DECRYPT_MODE, kg.generateKey(),
                new IvParameterSpec(getRandBuf(12)));
        testOutLen(cc20, 0, 0);
        testOutLen(cc20, 5, 0);
        testOutLen(cc20, 16, 0);
        testOutLen(cc20, 5120, 5104);
        
        
        cc20.update(input);
        testOutLen(cc20, 1024, 6128);
    }

    private static void testMultiPartAEADDec() throws GeneralSecurityException {
        KeyGenerator kg = KeyGenerator.getInstance("ChaCha20", "SunJCE");
        kg.init(256);
        Key key = kg.generateKey();
        IvParameterSpec ivps = new IvParameterSpec(getRandBuf(12));

        
        byte[] pText = getRandBuf(2048);
        ByteBuffer pTextBase = ByteBuffer.wrap(pText);

        Cipher enc = Cipher.getInstance("ChaCha20-Poly1305", "SunJCE");
        enc.init(Cipher.ENCRYPT_MODE, key, ivps);
        ByteBuffer ctBuf = ByteBuffer.allocateDirect(
                enc.getOutputSize(pText.length));
        enc.doFinal(pTextBase, ctBuf);

        
        
        ByteBuffer ptBuf = ByteBuffer.allocateDirect(pText.length);

        
        
        ctBuf.position(0).limit(1024);

        Cipher dec = Cipher.getInstance("ChaCha20-Poly1305", "SunJCE");
        dec.init(Cipher.DECRYPT_MODE, key, ivps);
        dec.update(ctBuf, ptBuf);
        System.out.println("CTBuf: " + ctBuf);
        System.out.println("PTBuf: " + ptBuf);
        ctBuf.limit(ctBuf.capacity());
        dec.doFinal(ctBuf, ptBuf);

        ptBuf.flip();
        pTextBase.flip();
        System.out.println("PT Base:" + pTextBase);
        System.out.println("PT Actual:" + ptBuf);

        if (pTextBase.compareTo(ptBuf) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Plaintext mismatch: Original: ").
                    append(pTextBase.toString()).append("\nActual :").
                    append(ptBuf);
            throw new RuntimeException(sb.toString());
        }
    }

    private static void testOutLen(Cipher c, int inLen, int expOut) {
        int actualOut = c.getOutputSize(inLen);
        if (actualOut != expOut) {
            throw new RuntimeException("Cipher " + c + ", in: " + inLen +
                    ", expOut: " + expOut + ", actual: " + actualOut);
        }
    }

    private static byte[] getRandBuf(int len) {
        byte[] buf = new byte[len];
        SR.nextBytes(buf);
        return buf;
    }
}

