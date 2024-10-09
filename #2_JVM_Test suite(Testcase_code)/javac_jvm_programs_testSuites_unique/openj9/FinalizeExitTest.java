
package j9vm.test.finalize;


public class FinalizeExitTest {
	String message;
	
	public void finalize() {
		System.out.println(message);
	}
	
	public FinalizeExitTest(String aString) {
		message = aString;
	}
	public FinalizeExitTest() {
		message = null;
	}
	
public static void main(String[] args) {
	System.runFinalizersOnExit(true);
	new FinalizeExitTest("anObject");
	new FinalizeExitTest("anotherObject");
}

}
