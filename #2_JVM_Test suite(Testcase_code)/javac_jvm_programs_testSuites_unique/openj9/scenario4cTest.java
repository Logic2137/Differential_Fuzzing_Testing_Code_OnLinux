
package j9vm.test.threadinterrupt;

class scenario4cTest {
	public static void main(String args[]) {
		scenario4cTest t = new scenario4cTest();
		t.Run();
	}

	public void Run() {
		Object a = new Object();
		Interrupter interrupter = new Interrupter(Thread.currentThread(), a);

		boolean madeItToSecondWait = false;

		synchronized (a) {
			interrupter.start();

			try {
				System.out.println("main  waiting 1st time");
				a.wait();
				System.out.println("main  done waiting first time");

				madeItToSecondWait = true;
				System.out.println("main  waiting 2nd time");
				a.wait();
				System.out.println("main  done waiting 2nd time");
				throw new Error("wait should have been interrupted");
			} catch (InterruptedException e) {
				if (madeItToSecondWait) {
					System.out.println("SUCCESS");
				} else {
					throw new Error(
							"main caught unexpected InterruptedException " + e);
				}
			}
		}
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
			}
		}
	}

}
