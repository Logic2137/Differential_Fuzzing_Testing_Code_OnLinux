
package com.evilprovider;

import java.security.*;
import java.security.spec.*;
import java.nio.ByteBuffer;
import javax.crypto.*;

public final class EvilHmacSHA1 extends MacSpi {

    private final Mac internalMac;

    public EvilHmacSHA1() throws GeneralSecurityException {
        internalMac = Mac.getInstance("HmacSHA1", "SunJCE");
    }

    @Override
    protected byte[] engineDoFinal() {
        return internalMac.doFinal();
    }

    @Override
    protected int engineGetMacLength() {
        return internalMac.getMacLength();
    }

    @Override
    protected void engineInit(Key key, AlgorithmParameterSpec spec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        SecretKey sKey;
        if (key instanceof SecretKey) {
            sKey = (SecretKey) key;
        } else {
            throw new IllegalArgumentException("Key must be a SecretKey");
        }
        byte[] sKeyEnc = sKey.getEncoded();
        int keyBits = sKeyEnc.length * 8;
        if (keyBits < 160) {
            throw new IllegalArgumentException("Key must be at least 160 bits");
        }
        internalMac.init(key, spec);
    }

    @Override
    protected void engineReset() {
        internalMac.reset();
    }

    @Override
    protected void engineUpdate(byte input) {
        internalMac.update(input);
    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len) {
        internalMac.update(input, offset, len);
    }

    @Override
    protected void engineUpdate(ByteBuffer input) {
        internalMac.update(input);
    }
}
