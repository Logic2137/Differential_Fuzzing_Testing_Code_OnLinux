
package j9vm.test.printstacktest;

public class SystemExitCallStackSizeTest {
	
	public static void main(String[] args) {
		
		SystemExitCallStackSizeTest test = new SystemExitCallStackSizeTest();
		test.test();
	}
	
	public class SystemExitCallerThread extends Thread{
		public void run() {
			System.exit(1);
		}
	}
	
	public void test(){
		Thread newThread = new SystemExitCallerThread();
		newThread.setName("SystemExitCallerThread");
		newThread.start();
	}

}
