

package nsk.jvmti.scenarios.bcinstr.BI01;

public class bi01t002a {

    static int additionalValue = 0;

    static public int methodA() {
        
        

        
        additionalValue = 1;

        
        

        int returnValue = 100 + additionalValue;
        return returnValue;
    }
}
