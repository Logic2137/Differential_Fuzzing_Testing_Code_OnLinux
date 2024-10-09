

package vm.runtime.defmeth.shared;


public enum ExecutionMode {
    DIRECT,            
    REFLECTION,        
    REDEFINITION,      
    INVOKE_EXACT,      
    INVOKE_GENERIC,    
    INVOKE_WITH_ARGS,  
    INDY;              

    public boolean isReflectionBased() {
        return (this == REFLECTION || this == INVOKE_WITH_ARGS);
    }
}
