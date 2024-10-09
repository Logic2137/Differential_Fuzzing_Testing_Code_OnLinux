
package j9vm.test.threadinterrupt;

class scenario4bTest {
	public static void main(String args[]) {
		scenario4bTest t = new scenario4bTest();
		t.Run();
	}

	public void Run() {
		Object a = new Object();
		Interrupter interrupter = new Interrupter(Thread.currentThread(), a);

		synchronized (a) {
			interrupter.start();
			try {
				System.out.println("main  waiting");
				a.wait(1000);
				System.out.println("main  done waiting");
			} catch (Exception e) {
				throw new Error("main shouldn't have been interrupted");
			}
		}
		System.out.println("SUCCESS");
	}

	class Interrupter extends Thread {
		Thread m_interruptee;
		Object m_a;

		Interrupter(Thread interruptee, Object a) {
			m_interruptee = interruptee;
			m_a = a;
		}

		public void run() {
			synchronized (m_a) {
				System.out.println("inter synced on a");
				System.out.println("inter notifying main");
				m_a.notify();
				System.out.println("inter done notifying main");
				System.out.println("inter notifying main");
				m_interruptee.interrupt();
				System.out.println("inter done interrupting main");
				System.out.println("inter sleeping for 5 seconds...");
				try {
					sleep(5 * 1000);
				} catch (Exception e) {
					System.out.println("!!! " + e);
				}
				System.out.println("inter done sleeping");
			}
		}
	}

}
