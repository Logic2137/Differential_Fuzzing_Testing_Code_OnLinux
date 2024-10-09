
package j9vm.test.threadinterrupt;

class scenario4Test {
	public static void main(String args[]) {
		scenario4Test t = new scenario4Test();
		t.Run();
	}

	static boolean s_afterBothEvents = false;

	public void Run() {
		Object a = new Object();
		Interrupter interrupter = new Interrupter(Thread.currentThread(), a);

		synchronized (a) {
			interrupter.start();

			try {
				System.out.println("main  waiting");
				a.wait();
				System.out.println("main  done waiting");
			} catch (Exception e) {
				throw new Error("main caught unexpected Exception:" + e);
			}
		}
		if (s_afterBothEvents) {
			System.out.println("SUCCESS");
		} else {
			throw new Error("main shouldn't have been able to lock a");
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
				s_afterBothEvents = true;
				System.out.println("inter done interrupting main");
			}
		}
	}

}
