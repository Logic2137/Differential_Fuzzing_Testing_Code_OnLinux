


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SecureRandomParameters;
import java.security.DrbgParameters;
import java.security.DrbgParameters.Instantiation;
import java.security.DrbgParameters.Capability;
import javax.crypto.Cipher;

public class ApiTest {

    private static final boolean SHOULD_PASS = true;
    private static final long SEED = 1l;
    private static final String INVALID_ALGO = "INVALID";
    private static final String DRBG_CONFIG = "securerandom.drbg.config";
    private static final String DRBG_CONFIG_VALUE
            = Security.getProperty(DRBG_CONFIG);

    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.egd", "file:/dev/urandom");

        if (args == null || args.length < 1) {
            throw new RuntimeException("No mechanism available to run test.");
        }
        String mech
                = "NATIVE".equals(args[0]) ? supportedNativeAlgo() : args[0];
        String[] algs = null;
        boolean success = true;

        try {
            if (!isDRBG(mech)) {
                SecureRandom random = SecureRandom.getInstance(mech);
                verifyAPI(random, mech);
                return;
            } else if (mech.equals("CTR_DRBG")) {
                algs = new String[]{"AES-128", "AES-192", "AES-256",
                    INVALID_ALGO};
            } else if (mech.equals("Hash_DRBG") || mech.equals("HMAC_DRBG")) {
                algs = new String[]{"SHA-224", "SHA-256", "SHA-512/224",
                    "SHA-512/256", "SHA-384", "SHA-512", INVALID_ALGO};
            } else {
                throw new RuntimeException(
                        String.format("Not a valid mechanism '%s'", mech));
            }
            runForEachMech(mech, algs);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            success = false;
        }

        if (!success) {
            throw new RuntimeException("At least one test failed.");
        }
    }

    
    private static void runForEachMech(String mech, String[] algs)
            throws Exception {
        for (String alg : algs) {
            runForEachAlg(mech, alg);
        }
    }

    private static void runForEachAlg(String mech, String alg)
            throws Exception {
        for (int strength : new int[]{-1, 0, 1, 223, 224,
            192, 255, 256}) {
            for (Capability cp : Capability.values()) {
                for (byte[] pr : new byte[][]{null, new byte[]{},
                    "personal".getBytes()}) {
                    SecureRandomParameters param
                            = DrbgParameters.instantiation(strength, cp, pr);
                    runForEachParam(mech, alg, param);
                }
            }
        }
    }

    private static void runForEachParam(String mech, String alg,
            SecureRandomParameters param) throws Exception {

        for (boolean df : new Boolean[]{true, false}) {
            try {
                Security.setProperty(DRBG_CONFIG, mech + "," + alg + ","
                        + (df ? "use_df" : "no_df"));
                System.out.printf("%nParameter for SecureRandom "
                        + "mechanism: %s is (param:%s, algo:%s, df:%s)",
                        mech, param, alg, df);
                SecureRandom sr = SecureRandom.getInstance("DRBG", param);
                verifyAPI(sr, mech);
            } catch (NoSuchAlgorithmException e) {
                
                checkException(getDefaultAlg(mech, alg), param, e);
            } finally {
                Security.setProperty(DRBG_CONFIG, DRBG_CONFIG_VALUE);
            }
        }
    }

    
    private static String getDefaultAlg(String mech, String alg)
            throws NoSuchAlgorithmException {
        if (alg == null) {
            switch (mech) {
                case "Hash_DRBG":
                case "HMAC_DRBG":
                    return "SHA-256";
                case "CTR_DRBG":
                    return (Cipher.getMaxAllowedKeyLength("AES") < 256)
                            ? "AES-128" : "AES-256";
                default:
                    throw new RuntimeException("Mechanism not supported");
            }
        }
        return alg;
    }

    
    private static void checkException(String alg, SecureRandomParameters param,
            NoSuchAlgorithmException e) throws NoSuchAlgorithmException {

        int strength = ((Instantiation) param).getStrength();
        boolean error = true;
        switch (alg) {
            case INVALID_ALGO:
                error = false;
                break;
            case "SHA-224":
            case "SHA-512/224":
                if (strength > 192) {
                    error = false;
                }
                break;
            case "SHA-256":
            case "SHA-512/256":
            case "SHA-384":
            case "SHA-512":
                if (strength > 256) {
                    error = false;
                }
                break;
            case "AES-128":
            case "AES-192":
            case "AES-256":
                int algoStrength = Integer.parseInt(alg.substring("AES-".length()));
                int maxAESStrength = Cipher.getMaxAllowedKeyLength("AES");
                if (strength > algoStrength
                        || algoStrength > maxAESStrength) {
                    error = false;
                }
                break;
        }
        if (error) {
            throw new RuntimeException("Unknown :", e);
        }
    }

    
    private static boolean isDRBG(String mech) {
        return mech.contains("_DRBG");
    }

    
    private static String supportedNativeAlgo() {
        String nativeSr = "Windows-PRNG";
        try {
            SecureRandom.getInstance(nativeSr);
        } catch (NoSuchAlgorithmException e) {
            nativeSr = "NativePRNG";
        }
        return nativeSr;
    }

    
    private static void verifyAPI(SecureRandom random, String mech)
            throws Exception {

        System.out.printf("%nTest SecureRandom mechanism: %s for provider: %s",
                mech, random.getProvider().getName());
        byte[] output = new byte[2];

        
        random.nextBytes(output);

        
        byte[] seed = random.generateSeed(1);
        random.setSeed(seed);
        random.nextBytes(output);

        
        random.setSeed(SEED);
        random.nextBytes(output);

        
        seed = random.generateSeed(128);
        random.setSeed(seed);
        random.nextBytes(output);

        
        
        
        
        matchExc(() -> {
            random.reseed();
            random.nextBytes(output);
        },
                isDRBG(mech),
                UnsupportedOperationException.class,
                String.format("PASS - Unsupported reseed() method for "
                        + "SecureRandom Algorithm %s ", mech));

        matchExc(() -> {
            random.reseed(DrbgParameters.reseed(false, new byte[]{}));
            random.nextBytes(output);
        },
                isDRBG(mech),
                UnsupportedOperationException.class,
                String.format("PASS - Unsupported reseed(param) method for "
                        + "SecureRandom Algorithm %s ", mech));

        matchExc(() -> {
            random.reseed(DrbgParameters.reseed(true, new byte[]{}));
            random.nextBytes(output);
        },
                isDRBG(mech),
                !isSupportPR(mech, random) ? IllegalArgumentException.class
                        : UnsupportedOperationException.class,
                String.format("PASS - Unsupported or illegal reseed(param) "
                        + "method for SecureRandom Algorithm %s ", mech));

        matchExc(() -> random.nextBytes(output,
                DrbgParameters.nextBytes(-1, false, new byte[]{})),
                isDRBG(mech),
                UnsupportedOperationException.class,
                String.format("PASS - Unsupported nextBytes(out, nextByteParam)"
                        + " method for SecureRandom Algorithm %s ", mech));

        matchExc(() -> random.nextBytes(output,
                DrbgParameters.nextBytes(-1, true, new byte[]{})),
                isDRBG(mech),
                !isSupportPR(mech, random) ? IllegalArgumentException.class
                        : UnsupportedOperationException.class,
                String.format("PASS - Unsupported or illegal "
                        + "nextBytes(out, nextByteParam) method for "
                        + "SecureRandom Algorithm %s ", mech));

        matchExc(() -> {
            random.reseed(null);
            random.nextBytes(output);
        },
                !SHOULD_PASS,
                IllegalArgumentException.class,
                "PASS - Test is expected to fail when parameter for reseed() "
                + "is null");

        matchExc(() -> random.nextBytes(output, null),
                !SHOULD_PASS,
                IllegalArgumentException.class,
                "PASS - Test is expected to fail when parameter for nextBytes()"
                + " is null");

    }

    private static boolean isSupportPR(String mech, SecureRandom random) {
        return (isDRBG(mech) && ((Instantiation) random.getParameters())
                .getCapability()
                .supportsPredictionResistance());
    }

    private interface RunnableCode {

        void run() throws Exception;
    }

    
    private static void matchExc(RunnableCode r, boolean shouldPass, Class ex,
            String msg) {
        try {
            r.run();
            if (!shouldPass) {
                throw new RuntimeException("Excecution should fail here.");
            }
        } catch (Exception e) {
            System.out.printf("%nOccured exception: %s - Expected exception: "
                    + "%s : ", e.getClass(), ex.getCanonicalName());
            if (ex.isAssignableFrom(e.getClass())) {
                System.out.printf("%n%s : Expected Exception occured: %s : ",
                        e.getClass(), msg);
            } else if (shouldPass) {
                throw new RuntimeException(e);
            } else {
                System.out.printf("Ignore the following exception: %s%n",
                        e.getMessage());
            }
        }
    }
}
