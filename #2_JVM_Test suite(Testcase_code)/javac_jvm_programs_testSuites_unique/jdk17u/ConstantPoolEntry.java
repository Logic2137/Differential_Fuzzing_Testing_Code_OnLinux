
package gc.stress.gcbasher;

class ConstantPoolEntry {

    private int index;

    private String value;

    public ConstantPoolEntry(int index) {
        this.index = index;
        value = null;
    }

    public ConstantPoolEntry(String value) {
        this.index = -1;
        this.value = value;
    }

    public String getValue() throws IllegalStateException {
        if (index != -1) {
            throw new IllegalStateException();
        }
        return value;
    }

    public int getNameIndex() throws IllegalStateException {
        if (value != null) {
            throw new IllegalStateException();
        }
        return index;
    }

    public int getClassIndex() throws IllegalStateException {
        if (value != null) {
            throw new IllegalStateException();
        }
        return index;
    }
}
