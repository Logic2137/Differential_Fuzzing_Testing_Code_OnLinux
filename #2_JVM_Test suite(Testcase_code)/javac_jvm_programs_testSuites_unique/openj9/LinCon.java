
package j9vm.test.hashCode.generator;


public class LinCon {
	final int RAND_MULT = 1103515245;
	final int RAND_ADD = 12345;

	public int runLinCon(int initialValue) {
		int temp = (initialValue * RAND_MULT) + RAND_ADD;
		return temp;
	}

	public int scrambleSeed(int initialValue) {
		int seed = initialValue;
		for (int i = 0; i < 41; ++i) {
			seed = runLinCon(seed);
		}
		return seed;
	}

}
