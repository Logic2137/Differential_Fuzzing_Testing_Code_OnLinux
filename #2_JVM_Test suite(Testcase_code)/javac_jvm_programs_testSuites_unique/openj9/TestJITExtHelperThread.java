
package j9vm.test.corehelper;

public class TestJITExtHelperThread {
	boolean isWaiting = false;
	Thread jittedThread = null;

	public void configureJittedHelperThread() {
		jittedThread = new Thread() {
			public void run() {
				waitForDump();
			}
		};

		jittedThread.start();

		
		synchronized(this) {
			while(!isWaiting) {
				try {
					wait(0,0);
				} catch (InterruptedException e) {}
			}
		}
	}

	void waitForDump() {
		synchronized(this) {
			
			isWaiting = true;
			notifyAll();

			while (isWaiting) {
				try {
					wait(0, 0);
				} catch (InterruptedException e) {}
			}
		}
	}

	public void endJittedHelperThread() {
		synchronized(this) {
			isWaiting = false;
			notifyAll();
		}

		try {
			jittedThread.join();
		} catch(InterruptedException e) {}
	}
}
