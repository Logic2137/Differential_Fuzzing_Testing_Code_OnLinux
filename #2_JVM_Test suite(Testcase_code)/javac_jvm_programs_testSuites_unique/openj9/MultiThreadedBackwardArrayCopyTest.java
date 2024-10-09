

package j9vm.test.arraycopy;
import java.util.Arrays;
import java.util.HashSet;



public class MultiThreadedBackwardArrayCopyTest implements Runnable {
	static Object[] source = new Object[] {
		createValidValue(1),
		createValidValue(2),
		createValidValue(3),
		createValidValue(4),
		createValidValue(5),
		createValidValue(6),
		createValidValue(7),
		createValidValue(8),
		createValidValue(9),
		createValidValue(10),
		createValidValue(11),
		createValidValue(12),
		createValidValue(13),
		createValidValue(14),
		createValidValue(15),
		createValidValue(16),
		createValidValue(17),
		createValidValue(18),
		createValidValue(19),
		createValidValue(20),
		createValidValue(21),
		createValidValue(22),
		createValidValue(23),
		createValidValue(24),
		createValidValue(25),
		createValidValue(26),
		createValidValue(27),
		createValidValue(28),
		createValidValue(29),
		createValidValue(30),
		createValidValue(31),
		createValidValue(32),
		createValidValue(33),
		createValidValue(34),
	};

	static HashSet<Object> validHashSet = new HashSet<Object>(Arrays.asList(source));
	String name;
	static int count = 1000000;
	MultiThreadedBackwardArrayCopyTest(String name) {
		this.name = name;
	}

	public static void main(String[] args) throws InterruptedException {
		new Thread(new MultiThreadedBackwardArrayCopyTest("Thread-1")).start();
		new Thread(new MultiThreadedBackwardArrayCopyTest("Thread-2")).start();
		new Thread(new MultiThreadedBackwardArrayCopyTest("Thread-3")).start();
		new Thread(new MultiThreadedBackwardArrayCopyTest("Thread-4")).start();
	}

	public void run() {
		Object[] dest = new Object[source.length];
		for (int i=0; i < count; i++) {
			rotateSource();
			System.arraycopy(source, 0, dest, 0, dest.length);
			for (int j=0; j < dest.length; j++){
				if (!validHashSet.contains(dest[j])){
					System.out.println("Error: Found unexpected object in array at " + j + " in " + name);
				}
			}
		}
	}


	
	private static Object createValidValue(int i) {
		
		byte [] unused = new byte[17832];
		return Integer.toString(i);
	}

	
	public static void rotateSource() {
		Object temp = source[source.length - 1]; 
		System.arraycopy(source, 1, source, 2, source.length - 2);
		source[1] = source[0];
		source[0] = temp;
	}
}
