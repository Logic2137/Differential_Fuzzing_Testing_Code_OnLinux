
package j9vm.test.threadinterrupt;

class scenario5Test {
	public static void main(String args[]) {
		scenario5Test t = new scenario5Test();
		t.Run();
	}

	public void Run() {
		Object a = new Object();
		Interrupter interrupter = new Interrupter(Thread.currentThread(), a);

		synchronized (a) {
			interrupter.start();
			try {
				System.out.println("main  waiting");
				a.wait();
				System.out.println("main  done waiting");
			} catch (InterruptedException e) {
				
				System.out.println("main  caught InterruptedException");
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
			try {
				synchronized (m_a) {
					System.out.println("inter synced on a");
					System.out.println("inter interrupting main");
					m_interruptee.interrupt();
					System.out.println("inter done interrupting main");
					sleep(1000);
					System.out.println("inter notifying main");
					m_a.notify();
					System.out.println("inter done notifying main");
				}
			} catch (Exception e) {
				System.out.println("Interrupter exception " + e);
			}
		}
	}

}
