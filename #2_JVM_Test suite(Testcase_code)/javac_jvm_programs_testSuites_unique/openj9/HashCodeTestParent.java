

package j9vm.test.hash;

public abstract class HashCodeTestParent {
	public static final int MODE_SYSTEM_GC = 0;
	public static final int MODE_SCAVENGE = 1;
	public static final int[] MODES = { MODE_SYSTEM_GC, MODE_SCAVENGE };
	
	private int mode;
	
	
	static Object obj;
	
	public HashCodeTestParent(int mode) {
		switch (mode) {
		case MODE_SCAVENGE:
		case MODE_SYSTEM_GC:
			this.mode = mode;
			break;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public void gc() {
		switch (mode) {
		case MODE_SYSTEM_GC:
			System.gc();
			System.gc();
			break;
		case MODE_SCAVENGE:
			long freeMemoryAtStart;
			int count = 0;
			do {
				
				freeMemoryAtStart = Runtime.getRuntime().freeMemory(); 
				obj = new byte[64];
			} while ( (count++ < 1000000) && (Runtime.getRuntime().freeMemory() <= freeMemoryAtStart));
			break;
		}
	}
	
}
