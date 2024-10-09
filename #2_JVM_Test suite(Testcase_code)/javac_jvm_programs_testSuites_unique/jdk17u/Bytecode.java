
package gc.g1.unloading.bytecode;

public class Bytecode {

    private String className;

    private byte[] bytecode;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public byte[] getBytecode() {
        return bytecode;
    }

    public void setBytecode(byte[] bytecode) {
        this.bytecode = bytecode;
    }

    public Bytecode(String className, byte[] bytecode) {
        super();
        this.className = className;
        this.bytecode = bytecode;
    }
}
