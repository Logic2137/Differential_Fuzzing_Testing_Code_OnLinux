
package j9vm.test.multivm;

public class MultiVMFibTest {

static {
	System.loadLibrary("j9mvm");
	staticInitializer();
}

private static native void staticInitializer();

private static native int otherVMFib(int value);

public static int fib(int value) {
	if (value < 3) return 1;
	return otherVMFib(value - 1) + otherVMFib(value - 2);
}

public static void main(String[] args) {
	int result = fib(12);

	System.out.println("Multi-VM fib(12) = " + result);
	if (result != 144) {
		System.out.println("*** Value should be 144 ***");
		throw new RuntimeException();
	}
}

}


