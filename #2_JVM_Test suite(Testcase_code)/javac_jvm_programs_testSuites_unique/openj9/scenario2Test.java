
package j9vm.test.threadinterrupt;

class scenario2Test {
	public static void main(String args[]) {
		scenario2Test t = new scenario2Test();
		t.Run();
	}

	public void Run() {
		Interrupter interrupter = new Interrupter(Thread.currentThread());

		interrupter.start();

		try {
			System.out.println("main  sleeping");
			Thread.sleep(100 * 1000);
			System.out.println("main  done sleeping");
			throw new Error("sleep should have been interrupted");
		} catch (InterruptedException ie) {
			System.out.println("SUCCESS");
		}
	}

	class Interrupter extends Thread {
		Thread m_interruptee;

		Interrupter(Thread interruptee) {
			m_interruptee = interruptee;
		}

		public void run() {
			System.out.println("inter waiting for main to be sleeping");
			try {
				sleep(3000);
			} catch (Exception e) {
				System.out.println("caught " + e);
			}
			System.out.println("inter interrupting main");
			m_interruptee.interrupt();
			System.out.println("inter done interrupting main");
		}
	}

}
