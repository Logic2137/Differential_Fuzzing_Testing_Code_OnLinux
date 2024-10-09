
package j9vm.test.threads.regression;

public class Sleeper {
	 public static void main(String[] args) {
		 try {                                                                 
		        Thread.sleep(4000);                                                  
		       } catch (InterruptedException e) {                                    
		        System.out.println(": Did not expect to be interrupted this time !!!!");             
		       } 
		       System.out.println("DONE");
	 }
}
