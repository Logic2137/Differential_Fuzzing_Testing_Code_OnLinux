

package j9vm.test.monitor;


public class Helpers {

	static {
		try {
			System.loadLibrary("j9ben");
		} catch (UnsatisfiedLinkError e) {
			System.out.println("No natives for JNI tests");
		}
	}
	
	public static native int getLastReturnCode();
	
	public static native int monitorEnter(Object obj);
	
	public static native int monitorExit(Object obj);
	
	public static native int monitorExitWithException(Object obj, Throwable throwable);
	
	public static native void monitorReserve(Object obj);
	
}
