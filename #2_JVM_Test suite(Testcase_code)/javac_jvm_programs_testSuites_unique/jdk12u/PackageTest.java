

package p;

public class PackageTest {
    public static void main(String args[]) {
        (new PackageTest()).test();
    }

    private void test() {
        ClassLoader cl = PackageTest.class.getClassLoader();
        Package pkg_from_loader;
        if (cl != null) {
            pkg_from_loader = cl.getDefinedPackage("p");
        } else {
            pkg_from_loader = Package.getPackage("p");
        }

        Package pkg = PackageTest.class.getPackage();
        if (pkg_from_loader != null && pkg == pkg_from_loader &&
            pkg.getName().equals("p")) {
            System.out.println("Expected package: " + pkg);
        } else {
            System.out.println("Unexpected package: " + pkg);
            System.exit(1);
        }
        if (pkg.isSealed()) {
            System.out.println("Package is sealed");
            System.exit(1);
        } else {
            System.out.println("Package is not sealed");
        }
    }
}
