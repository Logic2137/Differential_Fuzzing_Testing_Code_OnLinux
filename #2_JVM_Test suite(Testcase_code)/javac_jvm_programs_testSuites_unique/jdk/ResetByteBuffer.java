



import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.ByteBuffer;

public class ResetByteBuffer {

    Cipher c;
    SecretKey key;
    ByteBuffer in, out;
    byte[] data = new byte[1500];
    byte encrypted[];

    public static final void main(String args[]) throws Exception {
        
        
        String algo = "AES/GCM/NoPadding";
        new ResetByteBuffer(algo).decrypt(true).updateTest().updateTest();
        new ResetByteBuffer(algo).decrypt(false).updateTest().updateTest();
        new ResetByteBuffer(algo).decrypt(true).updateTest().doFinalTest();
        new ResetByteBuffer(algo).decrypt(false).updateTest().doFinalTest();
        new ResetByteBuffer(algo).decrypt(true).doFinalTest().updateTest();
        new ResetByteBuffer(algo).decrypt(false).doFinalTest().updateTest();
        new ResetByteBuffer(algo).decrypt(true).doFinalTest().doFinalTest();
        new ResetByteBuffer(algo).decrypt(false).doFinalTest().doFinalTest();

        
        
        algo = "AES/CBC/PKCS5Padding";
        new ResetByteBuffer(algo).encrypt(true).updateTest().updateTest();
        new ResetByteBuffer(algo).encrypt(false).updateTest().updateTest();
        new ResetByteBuffer(algo).encrypt(true).updateTest().doFinalTest();
        new ResetByteBuffer(algo).encrypt(false).updateTest().doFinalTest();
        new ResetByteBuffer(algo).encrypt(true).doFinalTest().updateTest();
        new ResetByteBuffer(algo).encrypt(false).doFinalTest().updateTest();
        new ResetByteBuffer(algo).encrypt(true).doFinalTest().doFinalTest();
        new ResetByteBuffer(algo).encrypt(false).doFinalTest().doFinalTest();
        new ResetByteBuffer(algo).decrypt(true).updateTest().updateTest();
        new ResetByteBuffer(algo).decrypt(false).updateTest().updateTest();
        new ResetByteBuffer(algo).decrypt(true).updateTest().doFinalTest();
        new ResetByteBuffer(algo).decrypt(false).updateTest().doFinalTest();
        new ResetByteBuffer(algo).decrypt(true).doFinalTest().updateTest();
        new ResetByteBuffer(algo).decrypt(false).doFinalTest().updateTest();
        new ResetByteBuffer(algo).decrypt(true).doFinalTest().doFinalTest();
        new ResetByteBuffer(algo).decrypt(false).doFinalTest().doFinalTest();
    }

    public ResetByteBuffer(String algo) throws Exception {
        c = Cipher.getInstance(algo);
        String a[] = algo.split("/");
        KeyGenerator kg = KeyGenerator.getInstance(a[0]);
        key = kg.generateKey();
        
        c.init(Cipher.ENCRYPT_MODE, key, c.getParameters());
        encrypted = new byte[c.getOutputSize(data.length)];
        c.doFinal(data, 0, data.length, encrypted, 0);
    }

    ResetByteBuffer decrypt(boolean direct) throws Exception {
        
        if (direct) {
            in = ByteBuffer.allocateDirect(encrypted.length);
            out = ByteBuffer.allocateDirect(encrypted.length);
        } else {
            in = ByteBuffer.allocate(encrypted.length);
            out = ByteBuffer.allocate(encrypted.length);
        }
        in.put(encrypted);
        in.flip();
        c.init(Cipher.DECRYPT_MODE, key, c.getParameters());
        return this;
    }

    ResetByteBuffer encrypt(boolean direct) throws Exception {
        
        if (direct) {
            in = ByteBuffer.allocateDirect(data.length);
            out = ByteBuffer.allocateDirect(c.getOutputSize(data.length));
        } else {
            in = ByteBuffer.allocate(data.length);
            out = ByteBuffer.allocate(c.getOutputSize(data.length));
        }
        c.init(Cipher.ENCRYPT_MODE, key, c.getParameters());
        return this;
    }

    ResetByteBuffer updateTest() throws Exception {
        int updateLen = data.length / 2;
        in.limit(updateLen);
        c.update(in, out);
        in.limit(in.capacity());
        c.doFinal(in, out);
        if (in.capacity() != in.position()) {
            System.out.println("There is data remaining in the input buffer");
        }
        if (out.limit() != out.position()) {
            System.out.println("There is data remaining in the output buffer");
        }
        in.flip();
        out.position(0);
        out.limit(out.capacity());
        return this;
    }

    ResetByteBuffer doFinalTest() throws Exception {
        c.doFinal(in, out);
        if (in.capacity() != in.position()) {
            System.out.println("There is data remaining in the input buffer");
        }
        if (out.limit() != out.position()) {
            System.out.println("There is data remaining in the output buffer");
        }
        in.flip();
        out.position(0);
        out.limit(out.capacity());
        return this;
    }
}
