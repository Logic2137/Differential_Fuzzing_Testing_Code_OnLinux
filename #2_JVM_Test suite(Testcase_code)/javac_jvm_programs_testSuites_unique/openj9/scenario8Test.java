
package j9vm.test.threadinterrupt;

class scenario8Test {
	public static void main(String args[]) {
		scenario8Test t = new scenario8Test();
		t.Run();
	}

	void Print(String s) {
		synchronized (System.out) {
			System.out.print(System.currentTimeMillis() + ": ");
			System.out.println(s);
		}
	}

	public void Run() {
		Object a = new Object();
		Object b = new Object();
		Interrupter interrupter = new Interrupter(Thread.currentThread(), b);
		interrupter.start();
		Owner owner = new Owner(a, b);
		owner.start();

		Print("main syncing on a ");
		synchronized (a) {
			Print("main synced on a ");
			try {
				Print("main  waiting");
				a.wait();
				Print("main  done waiting");
				throw new Error("wait should have been interrupted");
			} catch (InterruptedException ie) {
				Print("SUCCESS	 - Caught exception " + ie);
			}
		}
		Print(Thread.currentThread() + " (main) is done");
	}

	class Owner extends Thread {
		Object m_a;
		Object m_b;

		Owner(Object a, Object b) {
			m_a = a;
			m_b = b;
		}

		public void run() {
			try {
				sleep(2 * 1000);
				Print("owner syncing on a");
				synchronized (m_a) {
					Print("owner synced on a");
					Print("owner syncing on b");
					synchronized (m_b) {
						Print("owner synced on b");
						Print("owner releasing b");
					}
					Print("owner released b");
					Print("owner releasing a");
				}
				Print("owner released a");
			} catch (Exception e) {
				Print(this + " caught " + e);
			}
			Print(this + " (owner) is done");
		}
	}

	class Interrupter extends Thread {
		Thread m_interruptee;

		Object m_b;

		Interrupter(Thread interruptee, Object b) {
			m_interruptee = interruptee;
			m_b = b;
		}

		public void run() {
			try {
				sleep(1000);
				Print("inter syncing on b");
				synchronized (m_b) {
					Print("inter synced on b");
					sleep(6 * 1000);
					Print("inter interrupting main");
					m_interruptee.interrupt();
					Print("inter done interrupting main");
					Print("inter releasing b");
				}
				Print("inter released b");
				Print(this + " (inter) is done");
			} catch (Exception e) {
				Print("inter caught exception " + e);
			}
		}
	}
}
