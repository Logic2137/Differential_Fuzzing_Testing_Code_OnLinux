

import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



public class TestBootLayer {
    public static void main(String[] args) throws Exception {
        Pattern splitter = Pattern.compile(",");

        
        Set<String> modules = ModuleLayer.boot().modules().stream()
                .map(Module::getName)
                .collect(Collectors.toSet());

        
        splitter.splitAsStream(args[0])
                .filter(Predicate.not(String::isEmpty))
                .filter(mn -> !modules.contains(mn))
                .findAny()
                .ifPresent(mn -> {
                    throw new RuntimeException(mn + " not in boot layer!!!");
                });

        
        splitter.splitAsStream(args[1])
                .filter(Predicate.not(String::isEmpty))
                .filter(mn -> modules.contains(mn))
                .findAny()
                .ifPresent(mn -> {
                    throw new RuntimeException(mn + " in boot layer!!!!");
                });
    }
}
