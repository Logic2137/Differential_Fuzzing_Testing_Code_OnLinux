

import java.lang.Package;

public class PackageSealingTest {
    public static void main(String args[]) {
        try {
            Class c1 = PackageSealingTest.class.forName("sealed.pkg.C1");
            Class c2 = PackageSealingTest.class.forName("pkg.C2");
            Package p1 = c1.getPackage();
            System.out.println("Package 1: " + p1.toString());
            Package p2 = c2.getPackage();
            System.out.println("Package 2: " + p2.toString());

            if (!p1.isSealed()) {
                System.out.println("Failed: sealed.pkg is not sealed.");
                System.exit(0);
            }

            if (p2.isSealed()) {
                System.out.println("Failed: pkg is sealed.");
                System.exit(0);
            }

            System.out.println("OK");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
