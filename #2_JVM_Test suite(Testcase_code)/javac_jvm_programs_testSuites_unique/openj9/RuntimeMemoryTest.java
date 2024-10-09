
package j9vm.test.runtime;




public class RuntimeMemoryTest {

public static void main(String[] args) { 

	System.out.println("Testing for runtime memory natives..");
	try {
		System.out.println("Free Memory: " + Runtime.getRuntime().freeMemory());
		System.out.println("Total Memory: " + Runtime.getRuntime().totalMemory());
	} catch ( Error e ) {
		System.out.println("***FAILED***");
		throw new RuntimeException();
	}
		
}

}
