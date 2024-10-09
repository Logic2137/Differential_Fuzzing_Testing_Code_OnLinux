
package j9vm.test.cleanup;



public class NumberOfThreads {

	public static Thread main;

public static void main(java.lang.String[] args) {
	new NumberOfThreads().run();
}

public void run() {
	
	main = Thread.currentThread();
		
	(new Thread() { public void run() {
		while (true) {
			System.out.println("threads: " + Thread.currentThread().getThreadGroup().activeCount());
			System.out.println("isAlive: " + main.isAlive());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
	}}).start();

	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {}

		System.out.println("done");
	}
	
}
