
package j9vm.test.threadinterrupt;

class scenario1bTest {
	public static void main(String args[]) {
		scenario1bTest t = new scenario1bTest();
		t.Run();
	}

	volatile boolean m_interruptSent = false;

	public void Run() {
		Interruptee interruptee = new Interruptee();

		System.out.println("main starting interruptee");
		interruptee.start();
		System.out.println("main interrupting interruptee");
		interruptee.interrupt();
		m_interruptSent = true;
		System.out.println("main done interrupting interruptee");
	}

	class Interruptee extends Thread {
		public void run() {
			Object obj = new Object();
			try {
				System.out.println("interruptee spin waiting");
				while (!m_interruptSent) {
				}
				System.out.println("interruptee done spin waiting");
				synchronized (obj) {
					obj.wait();
				}
				System.out.println("interruptee done waiting");
				throw new Error("wait should have been interrupted");
			} catch (InterruptedException ie) {
				System.out.println("SUCCESS");
			}
		}
	}

}
