

package gc.stress.gcbasher;

class MethodInfo {
    private String name;
    private String descriptor;
    private int codeLength;
    private int codeStart;

    public MethodInfo(String name, String descriptor, int codeLength, int codeStart) {
        this.name = name;
        this.descriptor = descriptor;
        this.codeLength = codeLength;
        this.codeStart = codeStart;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public int getCodeStart() {
        return codeStart;
    }
}
