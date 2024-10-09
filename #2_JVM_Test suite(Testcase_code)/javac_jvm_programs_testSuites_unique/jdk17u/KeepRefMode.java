
package gc.g1.unloading.configuration;

public enum KeepRefMode {

    STRONG_REFERENCE,
    SOFT_REFERENCE,
    STATIC_FIELD,
    STACK_LOCAL,
    THREAD_FIELD,
    THREAD_ITSELF,
    STATIC_FIELD_OF_ROOT_CLASS,
    JNI_GLOBAL_REF,
    JNI_LOCAL_REF
}
