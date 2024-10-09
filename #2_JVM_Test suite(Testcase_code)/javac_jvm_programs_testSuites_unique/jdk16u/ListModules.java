

package listmods;

import static java.util.stream.Collectors.joining;

public class ListModules {
    public static void main(String[] args) throws Exception {
        System.out.println(ModuleLayer.boot()
                                .modules()
                                .stream()
                                .map(Module::getName)
                                .sorted()
                                .collect(joining("\n")));
    }
}
