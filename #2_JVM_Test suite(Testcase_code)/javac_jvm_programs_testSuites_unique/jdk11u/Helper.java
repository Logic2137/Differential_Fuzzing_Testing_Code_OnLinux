
package nsk.jvmti.ResourceExhausted;

public class Helper {

    static native boolean gotExhaustedEvent();
    static native void resetExhaustedEvent();

    static boolean checkResult(String eventName) {
        if ( ! gotExhaustedEvent() ) {
            System.err.println("Failure: Expected ResourceExhausted event after " + eventName + " did not occur");
            return false;
        }

        System.out.println("Got expected ResourceExhausted event");
        return true;
    }

}
