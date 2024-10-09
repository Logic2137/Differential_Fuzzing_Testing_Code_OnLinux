
package j9vm.test.libraryhandle;

import java.math.*;

public class MultipleLibraryLoadTest {

private static native String vmVersion();	

public static void main(String[] args) {
		try {
			System.loadLibrary("j9ben");
		} catch (UnsatisfiedLinkError e) {
			System.out.println("Problem opening JNI library first time");
			e.printStackTrace();
			throw new RuntimeException();
		}
		try {
			System.loadLibrary("j9ben");
		} catch (UnsatisfiedLinkError e) {
			System.out.println("Problem opening JNI library second time");
			e.printStackTrace();
			throw new RuntimeException();
		}

		
		new BigInteger("7");
		try {
			System.loadLibrary("j9int" + System.getProperty("com.ibm.oti.vm.library.version", "12"));
		} catch (UnsatisfiedLinkError e) {
			System.out.println("Caught expected UnsatisfiedLinkError.");
			return;
		}
		System.out.println("Able to open BigInteger library in different classLoader");
		throw new RuntimeException();
	}
	
}
