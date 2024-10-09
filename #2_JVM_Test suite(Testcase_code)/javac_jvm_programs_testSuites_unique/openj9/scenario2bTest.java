
package j9vm.test.threadinterrupt;

class scenario2bTest {
	public static void main(String args[]) {
		scenario2bTest t = new scenario2bTest();
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
			try {
				System.out.println("interruptee spin waiting");
				while (!m_interruptSent) {
				}
				System.out.println("interruptee done spin waiting");
				sleep(100 * 1000);
				System.out.println("interruptee done sleeping");
				throw new Error("sleep should have been interrupted");
			} catch (InterruptedException ie) {
				System.out.println("SUCCESS");
			}
		}
	}

}
