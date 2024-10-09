



import java.io.*;
import java.util.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.interfaces.PBEKey;

public class PKCS12Oid {
    private static String OID_PKCS12 = "1.2.840.113549.1.12.1.";
    private static String OID_PBEWithSHAAnd128BitRC4 = OID_PKCS12 + "1";
    private static String OID_PBEWithSHAAnd40BitRC4 = OID_PKCS12 + "2";
    private static String OID_PBEWithSHAAnd3KeyTripleDESCBC = OID_PKCS12 + "3";
    private static String OID_PBEWithSHAAnd128BitRC2CBC = OID_PKCS12 + "5";
    private static String OID_PBEWithSHAAnd40BitRC2CBC = OID_PKCS12 + "6";

    public static void main(String[] argv) throws Exception {
        Cipher c = Cipher.getInstance(OID_PBEWithSHAAnd40BitRC2CBC, "SunJCE");
        c = Cipher.getInstance(OID_PBEWithSHAAnd3KeyTripleDESCBC, "SunJCE");

        c = Cipher.getInstance(OID_PBEWithSHAAnd128BitRC4, "SunJCE");
        c = Cipher.getInstance(OID_PBEWithSHAAnd40BitRC4, "SunJCE");
        c = Cipher.getInstance(OID_PBEWithSHAAnd128BitRC2CBC, "SunJCE");
        System.out.println("All tests passed");
    }
}
