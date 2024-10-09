
package j9vm.test.threadinterrupt;

class scenario3Test {
	public static void main(String args[]) {
		scenario3Test t = new scenario3Test();
		t.Run();
	}

	
	static boolean s_afterWait = false;

	public void Run() {
		Object a = new Object();
		Interrupter interrupter = new Interrupter(Thread.currentThread(), a);

		synchronized (a) {
			interrupter.start();
			try {
				System.out.println("main  waiting");
				a.wait();
				System.out.println("main  done waiting");
				throw new Error("wait should have been interrupted");
			} catch (InterruptedException ie) {
				if (s_afterWait == true) {
					System.out.println("SUCCESS");
				} else {
					throw new Error("wait shouldn't be able to lock a");
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
			System.out.println("inter syncing on a");
			synchronized (m_a) {
				System.out.println("inter synced on a");
				System.out.println("inter interrupting main");
				m_interruptee.interrupt();
				System.out.println("inter done interrupting main");
				System.out.println("inter sleeping for 5 seconds");
				try {
					sleep(5 * 1000);
				} catch (Exception e) {
					System.out.println("!!! " + e);
				}
				s_afterWait = true;
				System.out.println("inter releasing a");
			}
			System.out.println("inter released a");
		}
	}

}
