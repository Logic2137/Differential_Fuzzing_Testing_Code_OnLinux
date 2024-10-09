
package j9vm.test.classunloading;


public class Counter {
	int count = 0;
	int desiredValue;
	public static Counter toCountUpTo(int desiredValue) {
		Counter counter = new Counter();
		counter.setDesiredValue(desiredValue);
		return counter;
	}
	public void setDesiredValue(int desiredValue) {
		this.desiredValue = desiredValue;
	}
	public synchronized int value() {
		return count;
	}
	public synchronized void increment() {
		count++;
	}
	public synchronized boolean atDesiredValue() {
		return count == desiredValue;
	}
}
