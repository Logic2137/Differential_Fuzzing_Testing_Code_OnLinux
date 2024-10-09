

public class JimageClassPackage {
    public static void main(String args[]) throws Throwable {
        
        
        
        
        
        
        
        String testcases[][] =
            {{"Loading shared boot module class first", "java.util",
              "java.util.Dictionary", "java.util.ServiceConfigurationError"},

             {"Loading shared app module class first", "sun.tools.javac",
              "sun.tools.javac.Main", "sun.tools.javac.BatchParser"},

             {"Loading shared ext module class first", "jdk.nio.zipfs",
              "jdk.nio.zipfs.ZipInfo", "jdk.nio.zipfs.ZipPath"},

             {"Loading non-shared boot module class first", "java.net",
              "java.net.HttpCookie", "java.net.URL"},

             {"Loading non-shared ext module class first", "com.sun.jndi.dns",
              "com.sun.jndi.dns.Resolver", "com.sun.jndi.dns.DnsName"}};

        JimageClassPackage test = new JimageClassPackage();
        for (int i = 0; i < testcases.length; i++) {
            System.out.println("Testcase " + i + ": " + testcases[i][0]);
            test.testPackage(testcases[i][1], testcases[i][2], testcases[i][3]);
        }
    }

    private void testPackage (String pkg,
                              String shared,
                              String nonShared) throws Throwable {
        Class c1 = Class.forName(shared);
        ClassLoader cl = c1.getClassLoader();
        Package pkg_from_loader;
        if (cl != null) {
            pkg_from_loader = cl.getDefinedPackage(pkg);
        } else {
            pkg_from_loader = Package.getPackage(pkg);
        }

        Package pkg_from_shared_class = c1.getPackage();

        Class c2 = Class.forName(nonShared);
        Package pkg_from_nonshared_class = c2.getPackage();

        if (pkg_from_loader != null &&
            pkg_from_shared_class != null &&
            pkg_from_loader == pkg_from_shared_class &&
            pkg_from_shared_class == pkg_from_nonshared_class &&
            pkg_from_shared_class.getName().equals(pkg)) {
            System.out.println("Expected package: " + pkg_from_shared_class.toString());
        } else {
            System.out.println("Unexpected package" + pkg_from_shared_class);
            System.exit(1);
        }
        if (pkg_from_shared_class.isSealed()) {
            System.out.println("Package is sealed");
        } else {
            System.out.println("Package is not sealed");
            System.exit(1);
        }
    }
}
