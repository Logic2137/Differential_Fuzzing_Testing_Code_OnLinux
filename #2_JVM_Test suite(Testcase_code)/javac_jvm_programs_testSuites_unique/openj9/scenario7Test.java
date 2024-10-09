
package j9vm.test.threadinterrupt;

class scenario7Test {
	public static void main(String args[]) {
		scenario7Test t = new scenario7Test();
		t.Run();
	}

	public void Run() {
		Object a = new Object();
		Interrupter interrupter = new Interrupter(Thread.currentThread(), a);
		interrupter.start();
		Notifier notifier = new Notifier(a);
		notifier.start();

		synchronized (a) {
			try {
				System.out.println("main  waiting");
				a.wait();
				System.out.println("main  done waiting");
			} catch (Exception e) {
				throw new Error("main caught unexpected exception: " + e);
			}
		}
		if (Thread.interrupted()) {
			System.out.println("SUCCESS");
		} else {
			throw new Error("main should be interrupted");
		}
	}

	class Notifier extends Thread {
		Object m_a;

		Notifier(Object a) {
			m_a = a;
		}

		public void run() {
			try {
				sleep(2 * 1000);

				System.out.println("noti syncing on a");
				synchronized (m_a) {
					System.out.println("noti synced on a");
					System.out.println("noti waiting on a");
					m_a.wait();
					System.out.println("noti notifying a");
					m_a.notify();
					System.out.println("noti done notifying a");
				}
			} catch (Exception e) {
				System.out.println(this + " caught " + e);
			}
			System.out.println(this + " (notifier) is done");
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
			try {
				sleep(4 * 1000);
			} catch (Exception e) {
				System.out.println(this + " caught " + e);
			}
			System.out.println("inter syncing on a");
			synchronized (m_a) {
				System.out.println("inter synced on a");
				System.out.println("inter notifying main");
				m_a.notifyAll();
				System.out.println("inter done notifying a");
				System.out.println("inter interrupting main");
				m_interruptee.interrupt();
				System.out.println("inter done interrupting main");
			}
			System.out.println(this + " (interrupter) is done");
		}
	}
}
