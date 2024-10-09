
package j9vm.test.jni;

public class NullRefTest  {

public static void main(String[] args) {
	try {
		System.loadLibrary("j9ben");

		test();
	} catch (UnsatisfiedLinkError e) {
		System.out.println("Problem opening JNI library");
		e.printStackTrace();
		throw new RuntimeException();
	}
	
	System.out.println("NewLocalRef, NewGlobalRef and NewWeakGlobalRef handle NULL references correctly");
}

public static native void test();

}
