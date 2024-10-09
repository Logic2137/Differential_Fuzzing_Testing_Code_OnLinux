
package j9vm.test.threadname;


public class ASCIIThreadNameTest {

	public void run() throws InterruptedException {
		String name = "";
		
		for (int i = 0; i < 10000; i++) {
			Thread myThread = new Thread(name) {
	        	public void run() {
	        	}
			};
			myThread.start();
			myThread.join();
			
			name = name + "X";
		}
	}

	public static void main(String[] args) throws Throwable {
		new ASCIIThreadNameTest().run();

	}

}
