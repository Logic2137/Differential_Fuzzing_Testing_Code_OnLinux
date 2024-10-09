
package vm.mlvm.meth.share;

import java.lang.invoke.MethodType;

public interface MethodParameterValueProvider {

    Object getValue(MethodType t, int paramNum);
}
