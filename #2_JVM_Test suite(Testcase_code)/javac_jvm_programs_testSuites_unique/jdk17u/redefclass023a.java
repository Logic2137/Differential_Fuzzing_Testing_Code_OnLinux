
package nsk.jvmti.RedefineClasses;

interface redefclass023j {

    public int getValue();
}

class redefclass023a implements redefclass023j {

    int intValue;

    public redefclass023a() {
        intValue = 0;
    }

    public void setValue(int i) {
        intValue = i;
    }

    public int getValue() {
        return intValue;
    }
}
