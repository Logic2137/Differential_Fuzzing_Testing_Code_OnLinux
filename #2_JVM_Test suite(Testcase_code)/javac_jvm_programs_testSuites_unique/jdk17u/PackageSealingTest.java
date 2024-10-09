import java.lang.Package;

public class PackageSealingTest {

    public static void main(String[] args) {
        if (args.length != 4) {
            throw new RuntimeException("Expecting 4 arguments");
        }
        try {
            Class c1 = PackageSealingTest.class.forName(args[0].replace('/', '.'));
            Class c2 = PackageSealingTest.class.forName(args[2].replace('/', '.'));
            Package p1 = c1.getPackage();
            System.out.println("Package 1: " + p1.toString());
            Package p2 = c2.getPackage();
            System.out.println("Package 2: " + p2.toString());
            if (args[1].equals("sealed") && !p1.isSealed()) {
                System.out.println("Failed: " + p1.toString() + " is not sealed.");
                System.exit(0);
            }
            if (args[3].equals("notSealed") && p2.isSealed()) {
                System.out.println("Failed: " + p2.toString() + " is sealed.");
                System.exit(0);
            }
            System.out.println("OK");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
