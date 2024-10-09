
package jdk.test.lib.util;

import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class ModuleUtils {

    private ModuleUtils() {
    }

    public static ModuleFinder finderOf(ModuleDescriptor... descriptors) {
        Map<String, ModuleReference> namesToReference = new HashMap<>();
        for (ModuleDescriptor descriptor : descriptors) {
            String name = descriptor.name();
            URI uri = URI.create("module:/" + name);
            ModuleReference mref = new ModuleReference(descriptor, uri) {

                @Override
                public ModuleReader open() {
                    throw new UnsupportedOperationException();
                }
            };
            namesToReference.put(name, mref);
        }
        return new ModuleFinder() {

            @Override
            public Optional<ModuleReference> find(String name) {
                Objects.requireNonNull(name);
                return Optional.ofNullable(namesToReference.get(name));
            }

            @Override
            public Set<ModuleReference> findAll() {
                return new HashSet<>(namesToReference.values());
            }
        };
    }
}
