
package jdk.dynalink.test;

import java.util.List;
import jdk.dynalink.linker.GuardingDynamicLinker;
import jdk.dynalink.linker.GuardingDynamicLinkerExporter;


public class UntrustedGuardingDynamicLinkerExporter extends GuardingDynamicLinkerExporter {
    @Override
    public List<GuardingDynamicLinker> get() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
