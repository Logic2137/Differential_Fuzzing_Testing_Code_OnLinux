
package j9vm.test.threadinterrupt;

class scenario1Test {
	public static void main(String args[]) {
		scenario1Test t = new scenario1Test();
		t.Run();
	}

	public void Run() {
		Object a = new Object();
		Interrupter interrupter = new Interrupter(Thread.currentThread());

		synchronized (a) {
			interrupter.start();

			try {
				System.out.println("main  waiting");
				a.wait();
				System.out.println("main  done waiting");
				throw new Error("wait should have been interrupted");
			} catch (InterruptedException ie) {
				System.out.println("SUCCESS");
			}
		}
	}

	class Interrupter extends Thread {
		Thread m_interruptee;

		Interrupter(Thread interruptee) {
			m_interruptee = interruptee;
		}

		public void run() {
			
			System.out.println("inter sleeping for 3 seconds...");
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
