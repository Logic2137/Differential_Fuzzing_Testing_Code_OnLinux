
package j9vm.test.jni;

public class LocalRefTest  {

public static void main(String[] args) {
	try {
		System.loadLibrary("j9ben");
		LocalRefTest test = new LocalRefTest();
		if( !test.testPushLocalFrame1()) {
			
			System.out.println("**FAILURE** JNI Push/PopLocalFrame not working, or weak JNI references don't work");
			throw new RuntimeException();
		}
		if( !test.testPushLocalFrame2()) {
			
			System.out.println("**FAILURE** JNI Push/PopLocalFrame not working, or weak JNI references don't work");
			throw new RuntimeException();
		}
		if( !test.testPushLocalFrame3()) {
			
			System.out.println("**FAILURE** JNI Push/PopLocalFrame not working, or weak JNI references don't work");
			throw new RuntimeException();
		}
		if( !test.testPushLocalFrame4()) {
			
			System.out.println("**FAILURE** JNI Push/PopLocalFrame not working, or weak JNI references don't work");
			throw new RuntimeException();
		}
		if( !test.testPushLocalFrameNeverPop()) {
			
			System.out.println("**FAILURE** JNI extra pushed frames not being cleaned up correctly");
			throw new RuntimeException();
		}
	} catch (UnsatisfiedLinkError e) {
		System.out.println("Problem opening JNI library");
		e.printStackTrace();
		throw new RuntimeException();
	}
	System.out.println("JNI Push/PopLocalFrame seem to work");
}
	
public native boolean testPushLocalFrame1();
public native boolean testPushLocalFrame2();
public native boolean testPushLocalFrame3();
public native boolean testPushLocalFrame4();
public native boolean testPushLocalFrameNeverPop();
}
