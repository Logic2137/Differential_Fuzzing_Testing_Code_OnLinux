

package org.openj9.test.openssl;

import java.util.Random;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoTest {
	public static void main(String[] args) {
		try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            Random r = new Random(10);
            byte[] skey_bytes = new byte[16];
            r.nextBytes(skey_bytes);
            SecretKeySpec skey = new SecretKeySpec(skey_bytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
			System.out.println("Crypto test COMPLETED");
		} catch (Exception e) {
			System.out.println("Crypto test FAILED");
			e.printStackTrace();
		}
	}
}
