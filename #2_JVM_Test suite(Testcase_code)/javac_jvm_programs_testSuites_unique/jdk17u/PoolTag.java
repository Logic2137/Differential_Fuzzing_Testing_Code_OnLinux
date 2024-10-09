
package jdk.experimental.bytecode;

public enum PoolTag {

    CONSTANT_UTF8(1),
    CONSTANT_UNICODE(2),
    CONSTANT_INTEGER(3),
    CONSTANT_FLOAT(4),
    CONSTANT_LONG(5),
    CONSTANT_DOUBLE(6),
    CONSTANT_CLASS(7),
    CONSTANT_STRING(8),
    CONSTANT_FIELDREF(9),
    CONSTANT_METHODREF(10),
    CONSTANT_INTERFACEMETHODREF(11),
    CONSTANT_NAMEANDTYPE(12),
    CONSTANT_METHODHANDLE(15),
    CONSTANT_METHODTYPE(16),
    CONSTANT_DYNAMIC(17),
    CONSTANT_INVOKEDYNAMIC(18);

    public final int tag;

    PoolTag(int tag) {
        this.tag = tag;
    }

    static PoolTag from(int tag) {
        return values()[tag - 1];
    }
}
