

import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckRuntimeVersion {
    public static void main(String... args) {
        int version = Integer.parseInt(args[0]);
        if (Runtime.version().major() != version) {
            throw new RuntimeException(version + " != current runtime version "
                + Runtime.version());
        }

        Set<String> expected = Arrays.stream(args, 1, args.length)
                                     .collect(Collectors.toSet());
        Set<String> modules = ModuleFinder.ofSystem().findAll().stream()
            .map(ModuleReference::descriptor)
            .map(ModuleDescriptor::name)
            .collect(Collectors.toSet());

        if (!modules.equals(expected)) {
            throw new RuntimeException("Expected: " + expected +
                " observable modules: " + modules);
        }
    }
}
