
package j9vm.test.printstacktest;

public class RunningThreadsStackSizeTest {

	
	public static void main(String[] args) {
		new RunningThreadsStackSizeTest().test();
	}

	public class RunningThread extends Thread{
		int delay = 0;
		public RunningThread(int d) {
			if(d >= 0)
				delay = d;
		}
		public void run() {
			synchronized (this) {
				this.notify();
				try {
					this.wait(delay);
				} catch (InterruptedException e) {
					return;
				}
			}			
		}
	}
	
	public void test(){
		
		RunningThread runningThread1 = new RunningThread(100000);
		runningThread1.setName("RunningThread1");
		synchronized (runningThread1) {
			runningThread1.start();
			try {
				runningThread1.wait();
			} catch (InterruptedException e) {
				System.err.println("The thread should not have been interrupted!");
			}
		}

		RunningThread runningThread2 = new RunningThread(100000);
		runningThread2.setName("RunningThread2");
		synchronized (runningThread2) {
			runningThread2.start();
			try {
				runningThread2.wait();
			} catch (InterruptedException e) {
				System.err.println("The thread should not have been interrupted!");
			}
		}
		System.exit(1);
	}
}
