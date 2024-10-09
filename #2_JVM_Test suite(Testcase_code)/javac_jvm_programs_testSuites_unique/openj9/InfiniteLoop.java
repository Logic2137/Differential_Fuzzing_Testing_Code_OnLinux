


public class InfiniteLoop {
	public static void main(String args[]) throws Exception {
		
		System.out.println("Running infinite loop");
		Object lock = new Object();
		synchronized (lock) {
			lock.wait();
		}
	}
}
