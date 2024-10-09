public class SealedUnnamedModuleIntfTest {

    public static void main(String[] args) throws Throwable {
        Class permitted = Class.forName("Pkg.Permitted");
        Class wrongPkg = Class.forName("otherPkg.WrongPackage");
        try {
            Class notPermitted = Class.forName("Pkg.NotPermitted");
            throw new RuntimeException("Expected IncompatibleClassChangeError exception not thrown");
        } catch (IncompatibleClassChangeError e) {
            if (!e.getMessage().contains("cannot implement sealed interface")) {
                throw new RuntimeException("Wrong IncompatibleClassChangeError exception thrown: " + e.getMessage());
            }
        }
        try {
            Class notPermitted = Class.forName("otherPkg.WrongPackageNotPublic");
            throw new RuntimeException("Expected IncompatibleClassChangeError exception not thrown");
        } catch (IncompatibleClassChangeError e) {
            if (!e.getMessage().contains("cannot implement sealed interface")) {
                throw new RuntimeException("Wrong IncompatibleClassChangeError exception thrown: " + e.getMessage());
            }
        }
    }
}
