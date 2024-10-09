

import java.io.IOException;
import java.lang.module.ModuleReference;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleReader;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;



class ModuleLibrary implements ModuleFinder {
    private final Map<String, ModuleReference> namesToReference = new HashMap<>();

    private ModuleLibrary() { }

    void add(ModuleDescriptor... descriptors) {
        for (ModuleDescriptor descriptor: descriptors) {
            String name = descriptor.name();
            if (!namesToReference.containsKey(name)) {
                

                URI uri = URI.create("module:/" + descriptor.name());

                ModuleReference mref = new ModuleReference(descriptor, uri) {
                    @Override
                    public ModuleReader open() {
                        throw new UnsupportedOperationException();
                    }
                };

                namesToReference.put(name, mref);
            }
        }
    }

    static ModuleLibrary of(ModuleDescriptor... descriptors) {
        ModuleLibrary ml = new ModuleLibrary();
        ml.add(descriptors);
        return ml;
    }

    @Override
    public Optional<ModuleReference> find(String name) {
        return Optional.ofNullable(namesToReference.get(name));
    }

    @Override
    public Set<ModuleReference> findAll() {
        return new HashSet<>(namesToReference.values());
    }
}

