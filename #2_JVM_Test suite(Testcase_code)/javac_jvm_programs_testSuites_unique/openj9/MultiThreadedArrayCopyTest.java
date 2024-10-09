

package j9vm.test.arraycopy;

import java.util.Arrays;
import java.util.HashSet;


public class MultiThreadedArrayCopyTest implements Runnable {

	static Object[] validValues = new Object[] {
		createValidValue(1),
		createValidValue(2),
		createValidValue(3),
		createValidValue(4),
		createValidValue(5),
		createValidValue(6),
		createValidValue(7),
		createValidValue(8),
		createValidValue(9),
		createValidValue(10)
	};
	
	static HashSet validHashSet = new HashSet(Arrays.asList(validValues));
	
	static Object[] source = (Object[])validValues.clone();

	static int count = 100000;
	
	String name;
	
	MultiThreadedArrayCopyTest(String name) {
		this.name = name;
	}
	
	public static void main(String[] args) {
		new Thread(new MultiThreadedArrayCopyTest("Thread-A")).start();
		new Thread(new MultiThreadedArrayCopyTest("Thread-B")).start();
		new Thread(new MultiThreadedArrayCopyTest("Thread-C")).start();
		new Thread(new MultiThreadedArrayCopyTest("Thread-D")).start();
	}

	public void run() {
		Object[] dest = new Object[validValues.length];
		for (int loop = 0; loop < count; loop++) {
			
			rotateSource(loop);
			System.arraycopy(source, 0, dest, 0, dest.length);
			
			for (int i = 0; i < dest.length; i++) {
				if (!validHashSet.contains(dest[i])) {
					System.err.println("Error: Found unexpected object in array at " + i + " in " + name);
					System.err.println("dest = " + Arrays.asList(dest));
				}
			}
		}
	}
	
	
	private static void rotateSource(int i) {
		int index = i % source.length;
		System.arraycopy(validValues, index, source, 0, source.length - index);
		System.arraycopy(validValues, 0, source, source.length - index, index);
	}
	
	
	private static Object createValidValue(int i) {
		
		byte[] unused = new byte[17832];
		return Integer.toString(i);
	}
	
	
}
