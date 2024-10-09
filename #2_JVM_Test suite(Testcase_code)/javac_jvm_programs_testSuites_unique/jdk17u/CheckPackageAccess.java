import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CheckPackageAccess {

    private static final SecurityManager sm = new SecurityManager();

    private static final ModuleFinder mf = ModuleFinder.ofSystem();

    private static final String[] EXPECTED = { "sun.misc.", "sun.reflect." };

    private static class Test {

        String moduleName;

        ModuleReference moduleRef;

        String exports;

        Optional<String> opens;

        String conceals;

        Optional<String> qualExports;

        Optional<String> qualOpens;

        Optional<String> qualOpensAndExports;

        Test(String module, String exports, String opens, String conceals, String qualExports, String qualOpens, String qualOpensAndExports) {
            this.moduleName = module;
            this.moduleRef = mf.find(moduleName).get();
            this.exports = exports;
            this.opens = Optional.ofNullable(opens);
            this.conceals = conceals;
            this.qualExports = Optional.ofNullable(qualExports);
            this.qualOpens = Optional.ofNullable(qualOpens);
            this.qualOpensAndExports = Optional.ofNullable(qualOpensAndExports);
        }

        void test() {
            final boolean isModulePresent = ModuleLayer.boot().findModule(moduleName).isPresent();
            System.out.format("Testing module: %1$s. Module is%2$s present.\n", moduleName, isModulePresent ? "" : " NOT");
            if (isModulePresent) {
                testNonRestricted(exports);
                opens.ifPresent(Test::testNonRestricted);
                testRestricted(conceals);
                qualExports.ifPresent(Test::testRestricted);
                qualOpens.ifPresent(Test::testRestricted);
                qualOpensAndExports.ifPresent(Test::testNonRestricted);
            } else {
                System.out.println("Skipping tests for module.");
            }
        }

        private static void testRestricted(String pkg) {
            try {
                sm.checkPackageAccess(pkg);
                throw new RuntimeException("Able to access restricted package: " + pkg);
            } catch (SecurityException se) {
            }
            try {
                sm.checkPackageDefinition(pkg);
                throw new RuntimeException("Able to access restricted package: " + pkg);
            } catch (SecurityException se) {
            }
        }

        private static void testNonRestricted(String pkg) {
            try {
                sm.checkPackageAccess(pkg);
            } catch (SecurityException se) {
                throw new RuntimeException("Unable to access exported package: " + pkg, se);
            }
            try {
                sm.checkPackageDefinition(pkg);
            } catch (SecurityException se) {
                throw new RuntimeException("Unable to access exported package: " + pkg, se);
            }
        }
    }

    private static final Test[] tests = new Test[] { new Test("java.base", "java.security", null, "jdk.internal.jrtfs", "jdk.internal.loader", null, null), new Test("java.desktop", "java.applet", null, "sun.font", "sun.awt", null, "javax.swing.plaf.basic"), new Test("java.security.jgss", "org.ietf.jgss", null, "sun.security.krb5.internal.crypto", "sun.security.krb5", null, null) };

    public static void main(String[] args) throws Exception {
        checkPackages(Arrays.asList(EXPECTED));
        for (Test test : tests) {
            test.test();
        }
        System.out.println("Test passed");
    }

    private static void checkPackages(List<String> pkgs) {
        for (String pkg : pkgs) {
            try {
                sm.checkPackageAccess(pkg);
                throw new RuntimeException("Able to access " + pkg + " package");
            } catch (SecurityException se) {
            }
            try {
                sm.checkPackageDefinition(pkg);
                throw new RuntimeException("Able to define class in " + pkg + " package");
            } catch (SecurityException se) {
            }
            String subpkg = pkg + "foo";
            try {
                sm.checkPackageAccess(subpkg);
                throw new RuntimeException("Able to access " + subpkg + " package");
            } catch (SecurityException se) {
            }
            try {
                sm.checkPackageDefinition(subpkg);
                throw new RuntimeException("Able to define class in " + subpkg + " package");
            } catch (SecurityException se) {
            }
        }
    }
}
