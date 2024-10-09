

package sun.security.ec;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;

import sun.security.util.ECUtil;
import static sun.security.util.SecurityProviderConstants.DEF_EC_KEY_SIZE;


public final class ECKeyPairGenerator extends KeyPairGeneratorSpi {

    private int keySize;

    public ECKeyPairGenerator() {
        initialize(DEF_EC_KEY_SIZE, null);
    }

    @Override
    public void initialize(int keySize, SecureRandom random) {
        this.keySize = keySize;
    }

    @Override
    public void initialize(AlgorithmParameterSpec params, SecureRandom random)
            throws InvalidAlgorithmParameterException {
        throw new UnsupportedOperationException();
    }

    @Override
    public KeyPair generateKeyPair() {
        BigInteger s, x, y;
        switch (keySize) {
            case 384:
                s = new BigInteger("230878276322370828604837367594276033697165"
                        + "328633328282930557390817326627704675451851870430805"
                        + "90262886393892128915463");
                x = new BigInteger("207573127814711182089888821916296502977037"
                        + "557291394001491584185306092085745595207966563387890"
                        + "64848861531410731137896");
                y = new BigInteger("272903686539605964684771543637437742229808"
                        + "792287657810480793861620950159864617021540168828129"
                        + "97920015041145259782242");
                break;
            default:
                throw new AssertionError("SunEC ECKeyPairGenerator" +
                    "has been patched. Key size " + keySize +
                    " is not supported");
        }
        ECParameterSpec ecParams = ECUtil.getECParameterSpec(null, keySize);
        try {
            return new KeyPair(new ECPublicKeyImpl(new ECPoint(x, y), ecParams),
                    new ECPrivateKeyImpl(s, ecParams));
        } catch (Exception ex) {
            throw new ProviderException(ex);
        }
    }
}
