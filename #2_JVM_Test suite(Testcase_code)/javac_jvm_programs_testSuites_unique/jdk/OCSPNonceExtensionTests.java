



import java.security.cert.Extension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import sun.security.util.DerValue;
import sun.security.util.DerInputStream;
import sun.security.util.ObjectIdentifier;
import sun.security.provider.certpath.OCSPNonceExtension;
import sun.security.x509.PKIXExtensions;

public class OCSPNonceExtensionTests {
    public static final boolean DEBUG = true;
    public static final String OCSP_NONCE_OID = "1.3.6.1.5.5.7.48.1.2";
    public static final String ELEMENT_NONCE = "nonce";
    public static final String EXT_NAME = "OCSPNonce";

    
    
    
    
    public static final byte[] OCSP_NONCE_DER = {
          48,   66,    6,    9,   43,    6,    1,    5,
           5,    7,   48,    1,    2,    1,    1,   -1,
           4,   50,    4,   48,  -34,  -83,  -66,  -17,
         -34,  -83,  -66,  -17,  -34,  -83,  -66,  -17,
         -34,  -83,  -66,  -17,  -34,  -83,  -66,  -17,
         -34,  -83,  -66,  -17,  -34,  -83,  -66,  -17,
         -34,  -83,  -66,  -17,  -34,  -83,  -66,  -17,
         -34,  -83,  -66,  -17,  -34,  -83,  -66,  -17,
         -34,  -83,  -66,  -17,
    };

    
    public static final byte[] DEADBEEF_16 = {
         -34,  -83,  -66,  -17,  -34,  -83,  -66,  -17,
         -34,  -83,  -66,  -17,  -34,  -83,  -66,  -17,
    };

    
    public static final byte[] OCSP_NONCE_DB16 = {
          48,   31,    6,    9,   43,    6,    1,    5,
           5,    7,   48,    1,    2,    4,   18,    4,
          16,  -34,  -83,  -66,  -17,  -34,  -83,  -66,
         -17,  -34,  -83,  -66,  -17,  -34,  -83,  -66,
         -17
    };

    public static void main(String [] args) throws Exception {
        Map<String, TestCase> testList =
                new LinkedHashMap<String, TestCase>() {{
            put("CTOR Test (provide length)", testCtorByLength);
            put("CTOR Test (provide nonce bytes)", testCtorByValue);
            put("CTOR Test (set criticality forms)", testCtorCritForms);
            put("CTOR Test (provide extension DER encoding)",
                    testCtorSuperByDerValue);
            put("Test getName() method", testGetName);
        }};

        System.out.println("============ Tests ============");
        int testNo = 0;
        int numberFailed = 0;
        Map.Entry<Boolean, String> result;
        for (String testName : testList.keySet()) {
            System.out.println("Test " + ++testNo + ": " + testName);
            result = testList.get(testName).runTest();
            System.out.print("Result: " + (result.getKey() ? "PASS" : "FAIL"));
            System.out.println(" " +
                    (result.getValue() != null ? result.getValue() : ""));
            System.out.println("-------------------------------------------");
            if (!result.getKey()) {
                numberFailed++;
            }
        }
        System.out.println("End Results: " + (testList.size() - numberFailed) +
                " Passed" + ", " + numberFailed + " Failed.");
        if (numberFailed > 0) {
            throw new RuntimeException(
                    "One or more tests failed, see test output for details");
        }
    }

    private static void dumpHexBytes(byte[] data) {
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (i % 16 == 0 && i != 0) {
                    System.out.print("\n");
                }
                System.out.print(String.format("%02X ", data[i]));
            }
            System.out.print("\n");
        }
    }

    private static void debuglog(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }

    public static void verifyExtStructure(byte[] derData) throws IOException {
        debuglog("verifyASN1Extension() received " + derData.length + " bytes");
        DerInputStream dis = new DerInputStream(derData);

        
        
        DerValue[] sequenceItems = dis.getSequence(3);
        debuglog("Found sequence containing " + sequenceItems.length +
                " elements");
        if (sequenceItems.length != 2 && sequenceItems.length != 3) {
            throw new RuntimeException("Incorrect number of items found in " +
                    "the SEQUENCE (Got " + sequenceItems.length +
                    ", expected 2 or 3 items)");
        }

        int seqIndex = 0;
        ObjectIdentifier extOid = sequenceItems[seqIndex++].getOID();
        debuglog("Found OID: " + extOid.toString());
        if (!extOid.equals((Object)PKIXExtensions.OCSPNonce_Id)) {
            throw new RuntimeException("Incorrect OID (Got " +
                    extOid.toString() + ", expected " +
                    PKIXExtensions.OCSPNonce_Id.toString() + ")");
        }

        if (sequenceItems.length == 3) {
            
            boolean isCrit = sequenceItems[seqIndex++].getBoolean();
            debuglog("Found BOOLEAN (critical): " + isCrit);
        }

        
        
        
        DerValue extnValue =
                new DerValue(sequenceItems[seqIndex++].getOctetString());
        byte[] nonceData = extnValue.getOctetString();
        debuglog("Found " + nonceData.length + " bytes of nonce data");
    }

    public interface TestCase {
        Map.Entry<Boolean, String> runTest();
    }

    public static final TestCase testCtorByLength = new TestCase() {
        @Override
        public Map.Entry<Boolean, String> runTest() {
            Boolean pass = Boolean.FALSE;
            String message = null;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                
                try {
                    Extension negLenNonce = new OCSPNonceExtension(-8);
                    throw new RuntimeException(
                            "Accepted a negative length nonce");
                } catch (IllegalArgumentException iae) { }

                
                try {
                    Extension zeroLenNonce = new OCSPNonceExtension(0);
                    throw new RuntimeException("Accepted a zero length nonce");
                } catch (IllegalArgumentException iae) { }

                
                try {
                    Extension bigLenNonce = new OCSPNonceExtension(33);
                    throw new RuntimeException("Accepted a larger than 32 bytes of nonce");
                } catch (IllegalArgumentException iae) { }

                
                Extension nonceByLen = new OCSPNonceExtension(32);

                
                nonceByLen.encode(baos);
                verifyExtStructure(baos.toByteArray());

                
                
                boolean crit = nonceByLen.isCritical();
                String oid = nonceByLen.getId();
                DerValue nonceData = new DerValue(nonceByLen.getValue());

                if (crit) {
                    message = "Extension incorrectly marked critical";
                } else if (!oid.equals(OCSP_NONCE_OID)) {
                    message = "Incorrect OID (Got " + oid + ", Expected " +
                            OCSP_NONCE_OID + ")";
                } else if (nonceData.getTag() != DerValue.tag_OctetString) {
                    message = "Incorrect nonce data tag type (Got " +
                            String.format("0x%02X", nonceData.getTag()) +
                            ", Expected 0x04)";
                } else if (nonceData.getOctetString().length != 32) {
                    message = "Incorrect nonce byte length (Got " +
                            nonceData.getOctetString().length +
                            ", Expected 32)";
                } else {
                    pass = Boolean.TRUE;
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
                message = e.getClass().getName();
            }

            return new AbstractMap.SimpleEntry<>(pass, message);
        }
    };

    public static final TestCase testCtorByValue = new TestCase() {
        @Override
        public Map.Entry<Boolean, String> runTest() {
            Boolean pass = Boolean.FALSE;
            String message = null;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                
                try {
                    Extension nullNonce = new OCSPNonceExtension(null);
                    throw new RuntimeException("Accepted a null nonce");
                } catch (NullPointerException npe) { }

                
                try {
                    Extension zeroLenNonce =
                            new OCSPNonceExtension(new byte[0]);
                    throw new RuntimeException("Accepted a zero length nonce");
                } catch (IllegalArgumentException iae) { }

                OCSPNonceExtension nonceByValue =
                        new OCSPNonceExtension(DEADBEEF_16);

                
                nonceByValue.encode(baos);
                verifyExtStructure(baos.toByteArray());

                
                
                boolean crit = nonceByValue.isCritical();
                String oid = nonceByValue.getId();
                byte[] nonceData = nonceByValue.getNonceValue();

                if (crit) {
                    message = "Extension incorrectly marked critical";
                } else if (!oid.equals(OCSP_NONCE_OID)) {
                    message = "Incorrect OID (Got " + oid + ", Expected " +
                            OCSP_NONCE_OID + ")";
                } else if (!Arrays.equals(nonceData, DEADBEEF_16)) {
                    message = "Returned nonce value did not match input";
                } else {
                    pass = Boolean.TRUE;
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
                message = e.getClass().getName();
            }

            return new AbstractMap.SimpleEntry<>(pass, message);
        }
    };

    public static final TestCase testCtorCritForms = new TestCase() {
        @Override
        public Map.Entry<Boolean, String> runTest() {
            Boolean pass = Boolean.FALSE;
            String message = null;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                Extension nonceByLength = new OCSPNonceExtension(true, 32);
                Extension nonceByValue =
                        new OCSPNonceExtension(true, DEADBEEF_16);
                pass = nonceByLength.isCritical() && nonceByValue.isCritical();
                if (!pass) {
                    message = "nonceByLength or nonceByValue was not marked " +
                            "critical as expected";
                }
            }  catch (Exception e) {
                e.printStackTrace(System.out);
                message = e.getClass().getName();
            }

            return new AbstractMap.SimpleEntry<>(pass, message);
        }
    };


    public static final TestCase testCtorSuperByDerValue = new TestCase() {
        @Override
        public Map.Entry<Boolean, String> runTest() {
            Boolean pass = Boolean.FALSE;
            String message = null;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                Extension nonceByDer = new sun.security.x509.Extension(
                        new DerValue(OCSP_NONCE_DER));

                
                nonceByDer.encode(baos);
                verifyExtStructure(baos.toByteArray());

                
                
                boolean crit = nonceByDer.isCritical();
                String oid = nonceByDer.getId();
                DerValue nonceData = new DerValue(nonceByDer.getValue());

                if (!crit) {
                    message = "Extension lacks expected criticality setting";
                } else if (!oid.equals(OCSP_NONCE_OID)) {
                    message = "Incorrect OID (Got " + oid + ", Expected " +
                            OCSP_NONCE_OID + ")";
                } else if (nonceData.getTag() != DerValue.tag_OctetString) {
                    message = "Incorrect nonce data tag type (Got " +
                            String.format("0x%02X", nonceData.getTag()) +
                            ", Expected 0x04)";
                } else if (nonceData.getOctetString().length != 48) {
                    message = "Incorrect nonce byte length (Got " +
                            nonceData.getOctetString().length +
                            ", Expected 48)";
                } else {
                    pass = Boolean.TRUE;
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
                message = e.getClass().getName();
            }

            return new AbstractMap.SimpleEntry<>(pass, message);
        }
    };

    public static final TestCase testGetName = new TestCase() {
        @Override
        public Map.Entry<Boolean, String> runTest() {
            Boolean pass = Boolean.FALSE;
            String message = null;
            try {
                OCSPNonceExtension nonceByLen = new OCSPNonceExtension(32);
                pass = new Boolean(nonceByLen.getName().equals(EXT_NAME));
            } catch (Exception e) {
                e.printStackTrace(System.out);
                message = e.getClass().getName();
            }

            return new AbstractMap.SimpleEntry<>(pass, message);
        }
    };
}
