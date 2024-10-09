
package j9vm.test.hashCode;

import java.io.PrintStream;

public class BitStatistics {

	long sampleCount;
	long disparity[];
	long sumPopCount;

	public BitStatistics() {
		disparity = new long[32];
		sampleCount = 0;
		sumPopCount = 0;
		for (int i = 0; i < 32; ++i) {
			disparity[i] = 0;
		}
	}

	public int update(int val) {
		int popCount = 0;
		for (int i = 0; i < 32; ++i) {
			if ((val & (1 << i)) != 0) {
				++popCount;
				disparity[i] += 1;
			} else {
				disparity[i] -= 1;
			}
		}
		sumPopCount += popCount;
		++sampleCount;
		return popCount;
	}

	public void printStats(PrintStream out, String context) {
		out.print(context + " bit disparities: ");
		int wordDisparity = 0;
		int wordAbsoluteDisparity = 0;
		for (int i = 0; i < 32; ++i) {
			long bitDisparity = disparity[i];
			wordDisparity += bitDisparity;
			wordAbsoluteDisparity += (bitDisparity < 0) ? -bitDisparity
					: bitDisparity;
			out.print(bitDisparity + ", ");
		}
		out.print('\n');
		String result = String.format(
				"%1$10s disparity: %2$8d average population count %3$3d",
				context, wordAbsoluteDisparity, sumPopCount / sampleCount);
		out.println(result);
		if (false) {
			out.print("\n" + context + " word disparity: " + wordDisparity);
			out.print(" word absolute disparity: " + wordAbsoluteDisparity);
			out.println(" average population count: " + sumPopCount
					/ sampleCount);
		}
	}
}
