import java.io.*;
import java.util.*;
import java.security.Security;
import java.security.cert.*;

public class TestPolicy {

    private final static String BASE = System.getProperty("test.src");

    private static CertificateFactory factory;

    private static X509Certificate loadCertificate(String name) throws Exception {
        InputStream in = new FileInputStream(new File(BASE, name));
        X509Certificate cert = (X509Certificate) factory.generateCertificate(in);
        in.close();
        return cert;
    }

    private static class TestCase {

        final String resultTree;

        final Set initialPolicies;

        TestCase(String resultTree, String p1, String p2, String p3) {
            this.resultTree = resultTree;
            this.initialPolicies = new HashSet();
            initialPolicies.add(p1);
            initialPolicies.add(p2);
            initialPolicies.add(p3);
            initialPolicies.remove(null);
        }

        public String toString() {
            return initialPolicies.toString();
        }
    }

    private final static TestCase[] TEST_CASES = new TestCase[] { new TestCase("2.5.29.32.0[1.2.0[1.2.0], 2.5.29.32.0[2.5.29.32.0]]", "2.5.29.32.0", null, null), new TestCase("2.5.29.32.0[1.2.0[1.2.0]]", "1.2.0", null, null), new TestCase("2.5.29.32.0[2.5.29.32.0[1.2.1]]", "1.2.1", null, null), new TestCase("2.5.29.32.0[1.2.0[1.2.0], 2.5.29.32.0[1.2.1]]", "1.2.0", "1.2.1", null), new TestCase("2.5.29.32.0[2.5.29.32.0[1.2.1, 1.2.2]]", "1.2.1", "1.2.2", null), new TestCase("2.5.29.32.0[1.2.0[1.2.0], 2.5.29.32.0[1.2.1, 1.2.2]]", "1.2.0", "1.2.1", "1.2.2") };

    public static void main(String[] args) throws Exception {
        Security.setProperty("jdk.certpath.disabledAlgorithms", "MD2");
        factory = CertificateFactory.getInstance("X.509");
        X509Certificate anchor = loadCertificate("anchor.cer");
        X509Certificate ca = loadCertificate("ca.cer");
        X509Certificate ee = loadCertificate("ee.cer");
        for (int i = 0; i < TEST_CASES.length; i++) {
            TestCase testCase = TEST_CASES[i];
            System.out.println("*** Running test: " + testCase);
            CertPathValidator validator = CertPathValidator.getInstance("PKIX");
            PKIXParameters params = new PKIXParameters(Collections.singleton(new TrustAnchor(anchor, null)));
            params.setRevocationEnabled(false);
            params.setInitialPolicies(testCase.initialPolicies);
            CertPath path = factory.generateCertPath(Arrays.asList(new X509Certificate[] { ee, ca }));
            PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult) validator.validate(path, params);
            PolicyNode tree = result.getPolicyTree();
            System.out.println(tree);
            String resultTree = toString(tree);
            if (resultTree.equals(testCase.resultTree) == false) {
                System.out.println("Mismatch");
                System.out.println("Should: " + testCase.resultTree);
                System.out.println("Is:     " + resultTree);
                throw new Exception("Test failed: " + testCase);
            }
        }
    }

    private static String toString(PolicyNode tree) {
        if (tree == null) {
            return "";
        }
        Iterator t = tree.getChildren();
        if (t.hasNext() == false) {
            return tree.getValidPolicy();
        }
        StringBuffer sb = new StringBuffer();
        List list = new ArrayList();
        while (t.hasNext()) {
            PolicyNode next = (PolicyNode) t.next();
            list.add(toString(next));
        }
        Collections.sort(list);
        return tree.getValidPolicy() + list;
    }
}
