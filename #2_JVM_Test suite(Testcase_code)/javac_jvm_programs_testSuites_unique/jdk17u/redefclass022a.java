
package nsk.jvmti.RedefineClasses;

class redefclass022a {

    int newValue;

    public redefclass022a() {
        newValue = 0;
    }

    public void setValue(int i) {
        newValue = i;
    }

    public int getValue() {
        return newValue;
    }
}
