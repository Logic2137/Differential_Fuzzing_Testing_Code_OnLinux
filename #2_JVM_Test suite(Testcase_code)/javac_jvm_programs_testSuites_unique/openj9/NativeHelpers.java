
package j9vm.test.thread;

public class NativeHelpers {
	static {
		try {
			System.loadLibrary("j9ben");
		} catch (UnsatisfiedLinkError e) {
			System.out.println("No natives for j9vm.test.thread tests");
		}
	}
	
	public static class DeadlockList {
		public Thread[] threads;
		public Object[] monitors;
		public DeadlockList() {
			threads = null;
			monitors = null;
		}
		public DeadlockList(Thread[] threads, Object[] monitors) {
			this.threads = threads;
			this.monitors = monitors;
		}
	}
	
	public static native Thread[] findDeadlockedThreads();
	public static native void findDeadlockedThreadsAndObjects(DeadlockList list);
}
