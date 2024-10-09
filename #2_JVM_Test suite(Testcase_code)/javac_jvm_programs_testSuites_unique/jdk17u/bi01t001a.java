
package nsk.jvmti.scenarios.bcinstr.BI01;

public class bi01t001a {

    public static int additionalValue = 0;

    public static int methodA() {
        additionalValue = 1;
        int returnValue = 100 + additionalValue;
        return returnValue;
    }
}
