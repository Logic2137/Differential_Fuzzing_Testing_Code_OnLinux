
package j9vm.test.hashCode.generator;


public class Lfsr {
	boolean shiftLeft = true;
	static final int GENERATOR = 0x04C11DB7;
	private static final int SHIFT_RIGHT_GENERATOR = 0xEDB88320;

	public int runLfsr(int initial, int iterations) {
		int newValue = initial;
		for (int i = 0; i < iterations; ++i) {
			if (shiftLeft) {
				if ((newValue & 0x80000000) != 0) {
					newValue <<= 1;
					newValue ^= GENERATOR;
				} else {
					newValue <<= 1;
				}
			} else {
				if ((newValue & 0x1) != 0) {
					newValue = (newValue >> 1) & 0x7fffffff;
					newValue ^= SHIFT_RIGHT_GENERATOR;
				} else {
					newValue = (newValue >> 1) & 0x7fffffff;
				}
			}
		}
		return newValue;
	}

	public int scrambleSeed(int initialValue) {
		int seed = runLfsr(initialValue, 40);
		return seed;
	}

	public void setShiftLeft(boolean doShiftLeft) {
		this.shiftLeft = doShiftLeft;
	}
}
