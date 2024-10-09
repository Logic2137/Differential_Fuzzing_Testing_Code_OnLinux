
package j9vm.test.jni;


public class LazyLinkTest {

	
	public static void main(String[] args) {

		try {
			
			
			System.out.println("Setup complete");

			System.loadLibrary("j9unresolved");

			System.out.println("Result: loaded");
		} catch (UnsatisfiedLinkError e) {
			System.out.println(e.getMessage());
			System.out.println("Result: unresolved");
		}
	}
}
