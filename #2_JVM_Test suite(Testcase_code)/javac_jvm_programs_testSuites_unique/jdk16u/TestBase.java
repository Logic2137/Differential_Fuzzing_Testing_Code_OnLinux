

import java.security.Policy;


public class TestBase {
    String filePath;
    boolean hasSM;
    String curDir;
    Policy origPolicy;

    String testName;
    static String errMessage;

    int passed = 0, failed = 0;

    
    public TestBase(String name) {
        testName = name;
    }

    
    protected void setUp() {
        if (System.getSecurityManager() != null) {
            hasSM = true;
            System.setSecurityManager(null);
        }

        filePath = System.getProperty("test.src");
        if (filePath == null) {
            
            filePath = System.getProperty("user.dir");
        }
        origPolicy = Policy.getPolicy();

    }

    
    public void tearDown() {
        
        System.setSecurityManager(null);
        Policy.setPolicy(origPolicy);
        if (hasSM) {
            System.setSecurityManager(new SecurityManager());
        }
        System.out.println("\nNumber of tests passed: " + passed);
        System.out.println("Number of tests failed: " + failed + "\n");

        if (errMessage != null ) {
            throw new RuntimeException(errMessage);
        }
    }

    void fail(String errMsg) {
        if (errMessage == null) {
            errMessage = errMsg;
        } else {
            errMessage = errMessage + "\n" + errMsg;
        }
        failed++;
    }

    void success(String msg) {
        passed++;
        System.out.println(msg);
    }

}
