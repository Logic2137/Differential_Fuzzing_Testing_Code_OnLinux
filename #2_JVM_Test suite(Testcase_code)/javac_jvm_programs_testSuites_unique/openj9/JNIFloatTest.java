
package j9vm.test.jni;

public class JNIFloatTest  {

public static void main(String[] args) {
	try {
		System.loadLibrary("j9ben");
		if( !floatJNITest() ) {
			
			System.out.println("**FAILURE** JNI Floats not working correctly");
			throw new RuntimeException();
			}
	} catch (UnsatisfiedLinkError e) {
		System.out.println("Problem opening JNI library");
		e.printStackTrace();
		throw new RuntimeException();
	}
}
	
public static native boolean floatJNITest();

}
