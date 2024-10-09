
package j9vm.test.terminate;


public class TerminateTest {

public static void main(String[] args) { 
	
	
	System.out.println("- - - - - - - - - - - - - -");
	System.out.println("Sacrificial process running");
	System.out.println("- - - - - - - - - - - - - -");
	for (int i = 0; i<180; i++)  {
		try  {
			Thread.sleep(1000);
		} catch (InterruptedException e)  {
			System.out.println("child sleep() interrupted");	
		}
		if ((i % 60) == 59)  {
			System.out.println(".");
		} else {
			System.out.print(".");
		}
	}
	System.out.println("");
	System.out.println("~3 mins but sacrificial process not killed!");
}

}
