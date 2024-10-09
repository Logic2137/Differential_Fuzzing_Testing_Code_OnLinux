
package intrinsics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class Verifier {

    enum VerificationStrategy {

        VERIFY_STRONG_EQUALITY {

            @Override
            void verify(Properties expectedProperties, int fullMatchCnt, int suspectCnt) {
                int expectedCount = Integer.parseInt(expectedProperties.getProperty(Verifier.INTRINSIC_EXPECTED_COUNT_PROPERTY));
                String intrinsicID = expectedProperties.getProperty(Verifier.INTRINSIC_NAME_PROPERTY);
                System.out.println("Intrinsic " + intrinsicID + " verification, expected: " + expectedCount + ", matched: " + fullMatchCnt + ", suspected: " + suspectCnt);
                if (expectedCount != fullMatchCnt) {
                    throw new RuntimeException("Unexpected count of intrinsic  " + intrinsicID + " expected:" + expectedCount + ", matched: " + fullMatchCnt + ", suspected: " + suspectCnt);
                }
            }
        }
        , VERIFY_INTRINSIC_USAGE {

            @Override
            void verify(Properties expectedProperties, int fullMatchCnt, int suspectCnt) {
                boolean isExpected = Boolean.parseBoolean(expectedProperties.getProperty(Verifier.INTRINSIC_IS_EXPECTED_PROPERTY));
                String intrinsicID = expectedProperties.getProperty(Verifier.INTRINSIC_NAME_PROPERTY);
                System.out.println("Intrinsic " + intrinsicID + " verification, is expected: " + isExpected + ", matched: " + fullMatchCnt + ", suspected: " + suspectCnt);
                if ((fullMatchCnt == 0 && isExpected) || (fullMatchCnt > 0 && !isExpected)) {
                    throw new RuntimeException("Unexpected count of intrinsic  " + intrinsicID + " is expected:" + isExpected + ", matched: " + fullMatchCnt + ", suspected: " + suspectCnt);
                }
            }
        }
        ;

        void verify(Properties expectedProperties, int fullMathCnt, int suspectCnt) {
            throw new RuntimeException("Default strategy is not implemented.");
        }
    }

    public static final String PROPERTY_FILE_SUFFIX = ".verify.properties";

    public static final String INTRINSIC_NAME_PROPERTY = "intrinsic.name";

    public static final String INTRINSIC_IS_EXPECTED_PROPERTY = "intrinsic.expected";

    public static final String INTRINSIC_EXPECTED_COUNT_PROPERTY = "intrinsic.expectedCount";

    private static final String DEFAULT_STRATEGY = VerificationStrategy.VERIFY_STRONG_EQUALITY.name();

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new RuntimeException("Test bug, nothing to verify");
        }
        for (String hsLogFile : args) {
            verify(hsLogFile);
        }
    }

    private static void verify(String hsLogFile) throws Exception {
        System.out.println("Verifying " + hsLogFile);
        Properties expectedProperties = new Properties();
        FileReader reader = new FileReader(hsLogFile + Verifier.PROPERTY_FILE_SUFFIX);
        expectedProperties.load(reader);
        reader.close();
        int fullMatchCnt = 0;
        int suspectCnt = 0;
        String intrinsicId = expectedProperties.getProperty(Verifier.INTRINSIC_NAME_PROPERTY);
        String prefix = "<intrinsic id='";
        String prefixWithId = prefix + intrinsicId + "'";
        try (BufferedReader compLogReader = new BufferedReader(new FileReader(hsLogFile))) {
            String logLine;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            while ((logLine = compLogReader.readLine()) != null) {
                if (logLine.startsWith(prefix)) {
                    if (logLine.startsWith(prefixWithId)) {
                        fullMatchCnt++;
                    } else {
                        suspectCnt++;
                        System.err.println("WARNING: Other intrinsic detected " + logLine);
                    }
                }
            }
        }
        VerificationStrategy strategy = VerificationStrategy.valueOf(System.getProperty("verificationStrategy", Verifier.DEFAULT_STRATEGY));
        strategy.verify(expectedProperties, fullMatchCnt, suspectCnt);
    }
}
