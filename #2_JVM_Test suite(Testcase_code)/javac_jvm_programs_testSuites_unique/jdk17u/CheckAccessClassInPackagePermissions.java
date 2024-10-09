import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleDescriptor.Exports;
import java.net.URL;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CheckAccessClassInPackagePermissions {

    private static final String[] deployModules = { "jdk.javaws", "jdk.plugin", "jdk.plugin.server", "jdk.deploy" };

    public static void main(String[] args) throws Exception {
        ModuleLayer bootLayer = ModuleLayer.boot();
        Set<Module> modules = bootLayer.modules().stream().filter(CheckAccessClassInPackagePermissions::isBootOrPlatformMod).collect(Collectors.toSet());
        Map<String, List<String>> map = new HashMap<>();
        Set<Exports> qualExports = modules.stream().map(Module::getDescriptor).map(ModuleDescriptor::exports).flatMap(Set::stream).filter(Exports::isQualified).collect(Collectors.toSet());
        for (Exports e : qualExports) {
            Set<String> targets = e.targets();
            for (String t : targets) {
                map.compute(t, (k, ov) -> {
                    if (ov == null) {
                        List<String> v = new ArrayList<>();
                        v.add(e.source());
                        return v;
                    } else {
                        ov.add(e.source());
                        return ov;
                    }
                });
            }
        }
        Policy policy = Policy.getPolicy();
        List<String> deployMods = Arrays.asList(deployModules);
        for (Map.Entry<String, List<String>> me : map.entrySet()) {
            String moduleName = me.getKey();
            if (deployMods.contains(moduleName)) {
                continue;
            }
            Optional<Module> module = bootLayer.findModule(moduleName);
            if (!module.isPresent()) {
                continue;
            }
            Module mod = module.get();
            if (mod.getClassLoader() != ClassLoader.getPlatformClassLoader()) {
                continue;
            }
            URL url = new URL("jrt:/" + moduleName);
            CodeSource cs = new CodeSource(url, (CodeSigner[]) null);
            ProtectionDomain pd = new ProtectionDomain(cs, null, null, null);
            List<String> pkgs = me.getValue();
            for (String p : pkgs) {
                RuntimePermission rp = new RuntimePermission("accessClassInPackage." + p);
                if (!policy.implies(pd, rp)) {
                    throw new Exception("Module " + mod + " has not been " + "granted " + rp);
                }
            }
        }
    }

    private static boolean isBootOrPlatformMod(Module m) {
        return m.getClassLoader() == null || m.getClassLoader() == ClassLoader.getPlatformClassLoader();
    }
}
