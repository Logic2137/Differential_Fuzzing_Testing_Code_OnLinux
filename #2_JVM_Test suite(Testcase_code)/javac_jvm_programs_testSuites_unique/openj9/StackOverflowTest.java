
package j9vm.test.stackoverflow;


public class StackOverflowTest {

	public void run() {
		recurse(1L,2L,3L);
	}

	int recurse(long a, long b, long c) {
		return recurse(a+a, b+b, c+recurse(a,b,c));
	}
	
	public static void main(String[] args) {
		try {
			new StackOverflowTest().run();
		} catch (StackOverflowError e) {
			System.out.println("Successfully caught StackOverflow exception");		
		} catch ( Error e ) {
			System.out.println("***FAILED***");
			throw new RuntimeException();
		}
	}
}
