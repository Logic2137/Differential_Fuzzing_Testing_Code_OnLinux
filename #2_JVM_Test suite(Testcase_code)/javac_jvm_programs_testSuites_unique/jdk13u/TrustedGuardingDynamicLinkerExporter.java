
package jdk.dynalink.test;

import java.util.List;
import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.linker.GuardingDynamicLinker;
import jdk.dynalink.linker.GuardingDynamicLinkerExporter;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;

public final class TrustedGuardingDynamicLinkerExporter extends GuardingDynamicLinkerExporter {

    private static final ThreadLocal<CallSiteDescriptor> lastDescriptor = new ThreadLocal<>();

    private static boolean enabled = false;

    public static void enable() {
        reset(true);
    }

    public static void disable() {
        reset(false);
    }

    public static boolean isLastCallSiteDescriptor(final CallSiteDescriptor desc) {
        return lastDescriptor.get() == desc;
    }

    private static void reset(final boolean enable) {
        lastDescriptor.set(null);
        enabled = enable;
    }

    @Override
    public List<GuardingDynamicLinker> get() {
        return List.of(((GuardingDynamicLinker) (final LinkRequest linkRequest, final LinkerServices linkerServices) -> {
            if (enabled) {
                lastDescriptor.set(linkRequest.getCallSiteDescriptor());
            }
            return null;
        }));
    }
}
