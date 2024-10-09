import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedefineModuleAgent {

    private static Instrumentation inst;

    public static void premain(String args, Instrumentation inst) throws Exception {
        RedefineModuleAgent.inst = inst;
    }

    static void redefineModule(Module module, Set<Module> extraReads, Map<String, Set<Module>> extraExports, Map<String, Set<Module>> extraOpens, Set<Class<?>> extraUses, Map<Class<?>, List<Class<?>>> extraProvides) {
        inst.redefineModule(module, extraReads, extraExports, extraOpens, extraUses, extraProvides);
    }

    static boolean isModifiableModule(Module module) {
        return inst.isModifiableModule(module);
    }
}
