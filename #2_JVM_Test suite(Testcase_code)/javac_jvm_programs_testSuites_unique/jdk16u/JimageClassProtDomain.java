

public class JimageClassProtDomain {
    public static void main(String args[]) throws Throwable {
        
        
        
        
        
        
        
        String testcases[][] =
            {{"Loading shared boot module class first",
              "java.util.Dictionary", "java.util.ServiceConfigurationError"},

             {"Loading shared app module class first",
              "com.sun.tools.javac.Main", "com.sun.tools.javac.code.Symbol"},

             {"Loading shared ext module class first",
              "jdk.nio.zipfs.ZipInfo", "jdk.nio.zipfs.ZipPath"},

             {"Loading non-shared boot module class first",
              "java.net.HttpCookie", "java.net.URL"},

             {"Loading non-shared app module class first",
              "com.sun.tools.sjavac.Util", "com.sun.tools.sjavac.Main"},

             {"Loading non-shared ext module class first",
              "com.sun.jndi.dns.Resolver", "com.sun.jndi.dns.DnsName"}};
        for (int i = 0; i < testcases.length; i++) {
            System.out.println("Testcase " + i + ": " + testcases[i][0]);
            JimageClassProtDomain.testProtectionDomain(testcases[i][1], testcases[i][2]);
        }
    }

    private static void testProtectionDomain(String shared, String nonShared)
              throws Throwable {
        Class c1 = Class.forName(shared);
        Class c2 = Class.forName(nonShared);
        if (c1.getProtectionDomain() != c2.getProtectionDomain()) {
            System.out.println(c1.getProtectionDomain());
            System.out.println(c1.getProtectionDomain().getCodeSource());
            System.out.println(c2.getProtectionDomain());
            System.out.println(c2.getProtectionDomain().getCodeSource());
            throw new RuntimeException("Failed: Protection Domains do not match!");
        } else {
            System.out.println(c1.getProtectionDomain());
            System.out.println(c1.getProtectionDomain().getCodeSource());
            System.out.println(c2.getProtectionDomain());
            System.out.println(c2.getProtectionDomain().getCodeSource());
            System.out.println("Passed: Protection Domains match.");
        }
    }
}
