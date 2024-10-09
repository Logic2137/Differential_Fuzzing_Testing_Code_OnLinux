



import java.io.*;
import java.util.*;
import java.security.cert.*;

import sun.security.x509.SerialNumber;
import sun.security.provider.certpath.*;



public class OCSPSingleExtensions {
    public static CertificateFactory CF;
    public static final File testDir =
            new File(System.getProperty("test.src", "."));
    public static final Base64.Decoder B64D = Base64.getMimeDecoder();

    public static void main(String [] args) throws Exception {
        
        CF = CertificateFactory.getInstance("X509");
        ByteArrayInputStream bais =
                new ByteArrayInputStream(readFile("int.crt").getBytes());
        X509Certificate intCA = (X509Certificate)CF.generateCertificate(bais);
        System.out.println("Successfully instantiated CA cert \"" +
                intCA.getSubjectX500Principal() + "\"");

        CertId cid0x1500 = new CertId(intCA, new SerialNumber(0x1500));
        boolean noFailures = true;

        OCSPResponse.SingleResponse sr =
                getSRByFilename("ocsp-good-nonext.resp", cid0x1500);
        noFailures &= checkSingleExts(sr, 0);

        if (sr.getRevocationTime() != null) {
            throw new RuntimeException("Oops. revocationTime is non-null " +
                    sr.getRevocationTime());
        } else if (sr.getRevocationReason() != null) {
            throw new RuntimeException("Oops. revocationReason is non-null " +
                    sr.getRevocationReason());
        }

        sr = getSRByFilename("ocsp-good-withnext.resp", cid0x1500);
        noFailures &= checkSingleExts(sr, 0);

        sr = getSRByFilename("ocsp-good-witharchcut.resp", cid0x1500);
        noFailures &= checkSingleExts(sr, 1);

        sr = getSRByFilename("ocsp-rev-nocerts.resp", cid0x1500);
        noFailures &= checkSingleExts(sr, 1);

        sr = getSRByFilename("ocsp-rev-nonext-noinv.resp", cid0x1500);
        noFailures &= checkSingleExts(sr, 0);

        sr = getSRByFilename("ocsp-rev-withnext-noinv.resp", cid0x1500);
        noFailures &= checkSingleExts(sr, 0);

        sr = getSRByFilename("ocsp-rev-nonext-withinv.resp", cid0x1500);
        noFailures &= checkSingleExts(sr, 1);

        sr = getSRByFilename("ocsp-rev-withnext-withinv.resp", cid0x1500);
        noFailures &= checkSingleExts(sr, 1);

        try {
            sr = getSRByFilename("ocsp-rev-twonext.resp", cid0x1500);
            System.out.println("FAIL: Allowed two nextUpdate fields");
            noFailures = false;
        } catch (IOException ioe) {
            System.out.println("Caught expected exception: " + ioe);
        }

        try {
            sr = getSRByFilename("ocsp-rev-bad-sr-tag.resp", cid0x1500);
            System.out.println("FAIL: Allowed invalid singleResponse item");
            noFailures = false;
        } catch (IOException ioe) {
            System.out.println("Caught expected exception: " + ioe);
        }

        try {
            sr = getSRByFilename("ocsp-rev-sr-cont-reverse.resp", cid0x1500);
            System.out.println("FAIL: Allowed reversed " +
                    "nextUpdate/singleExtensions");
            noFailures = false;
        } catch (IOException ioe) {
            System.out.println("Caught expected exception: " + ioe);
        }

        if (!noFailures) {
            throw new RuntimeException("One or more tests failed");
        }
    }

    private static OCSPResponse.SingleResponse getSRByFilename(String fileName,
            CertId cid) throws IOException {
        byte[] respDER = B64D.decode(readFile(fileName));
        OCSPResponse or = new OCSPResponse(respDER);
        OCSPResponse.SingleResponse sr = or.getSingleResponse(cid);
        return sr;
    }

    private static String readFile(String fileName) throws IOException {
        String filePath = testDir + "/" + fileName;
        StringBuilder sb = new StringBuilder();

        try (FileReader fr = new FileReader(filePath);
                BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().startsWith("#")) {
                    sb.append(line).append("\n");
                }
            }
        }

        System.out.println("Successfully read " + fileName);
        return sb.toString();
    }

    private static boolean checkSingleExts(OCSPResponse.SingleResponse sr,
            int singleExtCount) {
        Map<String, Extension> singleExts;
        try {
            singleExts = sr.getSingleExtensions();
        } catch (NullPointerException npe) {
            System.out.println(
                    "Warning: Sent null singleResponse into checkSingleExts");
            return false;
        }

        for (String key : singleExts.keySet()) {
            System.out.println("singleExtension: " + singleExts.get(key));
        }

        if (singleExts.size() != singleExtCount) {
            System.out.println("Single Extension count mismatch, " +
                    "expected " + singleExtCount + ", got " +
                    singleExts.size());
            return false;
        } else {
            return true;
        }
    }
}
