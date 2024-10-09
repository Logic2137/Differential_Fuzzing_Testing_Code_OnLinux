
package j9vm.test.loadLibrary;


public class TestLoadLibrary {
	static {
		System.loadLibrary("abcdef");
	}

	public static void main(String[] args) {
		System.out.println("hello");
	}
}
