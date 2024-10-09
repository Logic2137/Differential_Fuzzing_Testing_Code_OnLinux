

package j9vm.test.jni;

public class GetObjectRefTypeTest {
	public static void main(String[] args) {
		try {
			System.loadLibrary("j9ben");
			if( !getObjectRefTypeTest(new Object()) ) {
				
				System.out.println("**FAILURE** JNI GetObjectRefType not working correctly");
				throw new RuntimeException();
				}
		} catch (UnsatisfiedLinkError e) {
			System.out.println("Problem opening JNI library");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
		
	public static native boolean getObjectRefTypeTest(Object stackArg);
}
