
package j9vm.test.arraylets;


public class ArrayletAllocateTest {
	private static final int rangeSize = 32;
	
	
	public static byte[] bytes = null;
	public static short[] shorts = null;
	public static int[] ints = null;
	public static long[] longs = null;
	public static Object[] objects = null;
	
	public static void main(String[] args) {
		long maxMemory = Runtime.getRuntime().freeMemory();
		
		
		System.out.println("Testing array allocation...");
		long maxMemoryForFirstTest = maxMemory / 8 / 2;
		for (int logSize = 0; logSize < 31; logSize++) {
			int maxSize = 1 << logSize;
			
			if ((maxSize >= rangeSize) && (maxSize < maxMemoryForFirstTest)) {
				int minSize = Math.max(1, maxSize - rangeSize);
				System.out.println("\tTesting arrays from " + minSize + " to " + maxSize + "...");
				for (int size = minSize; size <= maxSize; size++) {
					test(new byte[size]);
					test(new short[size]);
					test(new int[size]);
					test(new long[size]);
					test(new Object[size]);
				}
			}
		}
		System.out.println("Array allocation tests passed.");
		
		System.out.println("Testing hashed array allocation and growth...");
		long maxMemoryForSecondTest = maxMemoryForFirstTest / 2;
		for (int logSize = 0; logSize < 31; logSize++) {
			int maxSize = 1 << logSize;
			
			if ((maxSize >= rangeSize) && (maxSize < maxMemoryForSecondTest)) {
				int minSize = Math.max(1, maxSize - rangeSize);
				System.out.println("\tTesting arrays from " + minSize + " to " + maxSize + "...");
				for (int size = minSize; size <= maxSize; size++) {
					bytes = new byte[size];
					bytes.hashCode();
					test(bytes);
					shorts = new short[size];
					shorts.hashCode();
					test(shorts);
					ints = new int[size];
					ints.hashCode();
					test(ints);
					longs = new long[size];
					longs.hashCode();
					test(longs);
					objects = new Object[size];
					objects.hashCode();
					test(objects);
				}
			}
		}
		System.out.println("Hashed array allocation and growth tests passed.");
	}

	private static void test(byte[] array) {
		array[0] = 1;
		array[array.length - 1] = 1;
	}

	private static void test(short[] array) {
		array[0] = 1;
		array[array.length - 1] = 1;
	}
	
	private static void test(int[] array) {
		array[0] = 1;
		array[array.length - 1] = 1;
	}

	private static void test(long[] array) {
		array[0] = 1;
		array[array.length - 1] = 1;
	}

	private static void test(Object[] array) {
		array[0] = "1";
		array[array.length - 1] = "1";
	}
}
