
package j9vm.test.clinit;




class NewTestHelper {
	public static String i;

	static {
		NewDuringClinit.jitNew();
		Object lock = NewDuringClinit.lock;
		synchronized(lock) {
			NewDuringClinit.runBlocker = true;
			lock.notifyAll();
		}
		for (int i = 0; i < 100000; ++i) {
			try {
				Thread.sleep(1000000);
			} catch(InterruptedException e) {
			}
		}
	}
}

public class NewDuringClinit {
	public static Object lock = new Object();
	public static boolean runBlocker = false;
	public static boolean threadsReady = false;
	public static boolean passed = true;

	public static void jitNew() {
		try {
			
			
			new NewTestHelper().notifyAll();
		} catch(IllegalMonitorStateException e) {
		}
	}

	public static void test() {
		Thread initializer = new Thread() {
			public void run() {
				
				try {
					Class.forName("j9vm.test.clinit.NewTestHelper");
				} catch(ClassNotFoundException t) {
					passed = false;
					throw new RuntimeException(t);
				}
			}
		};
		Thread blocker = new Thread() {
			public void run() {
				synchronized(lock) {
					while (!runBlocker) {
						try {
							lock.wait();
						} catch(InterruptedException e) {
						}
					}
					threadsReady = true;
					lock.notifyAll();
				}
				jitNew();
				
				
				passed = false;
			}
		};
		initializer.start();
		blocker.start();
		
		synchronized(lock) {
			while (!threadsReady) {
				try {
					lock.wait();
				} catch(InterruptedException e) {
				}
			}
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		initializer.stop();
		blocker.stop();
		try {
			initializer.join(); 
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			blocker.join(); 
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		if (!passed) {
			throw new RuntimeException("TEST FAILED");
		}
	}

	public static void main(String[] args) {
		NewDuringClinit.test();
	}
}
