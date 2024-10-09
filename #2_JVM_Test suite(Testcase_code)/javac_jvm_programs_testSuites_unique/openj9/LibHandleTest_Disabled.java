
package j9vm.test.libraryhandle;


public class LibHandleTest_Disabled {

public static void main(String[] args) { 
	try {
		System.loadLibrary("j9ben");
		int result = libraryHandleTest("j9ben");
		if( result != 0) {
			
			System.out.println("**FAILURE** reason = " + result);
			throw new RuntimeException();
			}
	} catch (UnsatisfiedLinkError e) {
		System.out.println("Problem opening JNI library");
		e.printStackTrace();
		throw new RuntimeException();
	}
}
	
public static native int libraryHandleTest(String libName);

}
