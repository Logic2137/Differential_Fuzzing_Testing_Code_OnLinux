
package jdk.dynalink.test;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.NamedOperation;
import jdk.dynalink.NamespaceOperation;
import jdk.dynalink.Operation;
import jdk.dynalink.StandardNamespace;
import jdk.dynalink.StandardOperation;
import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.GuardingDynamicLinker;
import jdk.dynalink.linker.GuardingDynamicLinkerExporter;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;
import jdk.dynalink.linker.support.SimpleLinkRequest;

public final class TrustedUnderscoreNameLinkerExporter extends GuardingDynamicLinkerExporter {

    private static final Pattern UNDERSCORE_NAME = Pattern.compile("_(.)");

    private static String translateToCamelCase(final String name) {
        final Matcher m = UNDERSCORE_NAME.matcher(name);
        final StringBuilder buf = new StringBuilder();
        while (m.find()) {
            m.appendReplacement(buf, m.group(1).toUpperCase());
        }
        m.appendTail(buf);
        return buf.toString();
    }

    @Override
    public List<GuardingDynamicLinker> get() {
        final ArrayList<GuardingDynamicLinker> linkers = new ArrayList<>();
        linkers.add(new GuardingDynamicLinker() {

            @Override
            public GuardedInvocation getGuardedInvocation(final LinkRequest request, final LinkerServices linkerServices) throws Exception {
                final CallSiteDescriptor desc = request.getCallSiteDescriptor();
                final Operation op = desc.getOperation();
                final Object name = NamedOperation.getName(op);
                final Operation namespaceOp = NamedOperation.getBaseOperation(op);
                final boolean isGetMethod = NamespaceOperation.getBaseOperation(namespaceOp) == StandardOperation.GET && StandardNamespace.findFirst(namespaceOp) == StandardNamespace.METHOD;
                if (isGetMethod && name instanceof String) {
                    final String str = (String) name;
                    if (str.indexOf('_') == -1) {
                        return null;
                    }
                    final String nameStr = translateToCamelCase(str);
                    final CallSiteDescriptor newDesc = AccessController.doPrivileged(new PrivilegedAction<CallSiteDescriptor>() {

                        @Override
                        public CallSiteDescriptor run() {
                            return desc.changeOperation(((NamedOperation) op).changeName(nameStr));
                        }
                    });
                    final LinkRequest newRequest = request.replaceArguments(newDesc, request.getArguments());
                    return linkerServices.getGuardedInvocation(newRequest);
                }
                return null;
            }
        });
        return linkers;
    }
}
