


import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;



public class TestRootModules {
    public static void main(String[] args) {
        
        
        ModuleLayer bootLayer = ModuleLayer.boot();
        ModuleFinder.ofSystem().findAll().stream()
            .map(ModuleReference::descriptor)
            .filter(descriptor -> descriptor.exports()
                    .stream()
                    .filter(e -> !e.isQualified())
                    .findAny()
                    .isPresent())
            .map(ModuleDescriptor::name)
            .forEach(name -> {
                if (!bootLayer.findModule(name).isPresent())
                    throw new RuntimeException(name + " not in boot layer");
            });

        
        ModuleLayer.boot()
                .findModule("java.se")
                .map(m -> { throw new RuntimeException("java.se should not be resolved"); });
    }
}
