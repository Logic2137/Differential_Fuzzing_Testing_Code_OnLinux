
package j9vm.test.hashCode;

import java.io.PrintStream;


public class Statistics {
	

	long count, maximum, minimum, sum, sumOfSquares;
	String variableName;
	
	
	public Statistics(String name) {
		this.variableName = name;
		count = 0;
		maximum = Long.MIN_VALUE;
		minimum = Long.MAX_VALUE;
		sum = 0;
		sumOfSquares = 0;
	}
	
	
	public void add(long newValue) {
		sum += newValue;
		sumOfSquares += newValue*newValue;
		maximum = java.lang.Math.max(newValue, maximum);
		minimum = java.lang.Math.min(newValue, minimum);
		++count;
	}

	
	public long getCount() {
		return count;
	}

	
	public long getMaximum() {
		return maximum;
	}

	
	public long getMinimum() {
		return minimum;
	}

	
	public long getSum() {
		return sum;
	}

	
	public long getMean() {
		return (count > 0)? (sum/count): 0;
	}

	
	public long getVariance() {
		long variance = 0;
		if (count > 1) {
			variance = ((count * sumOfSquares) -  (sum*sum))/(count * (count-1));
		}
		return variance;
	}

	
	public long getStandardDeviation() {
		
		return (long) java.lang.Math.sqrt(getVariance());
	}

	
	public String getVariableName() {
		return variableName;
	}
	
	
	public void printStatistics(PrintStream output) {
		output.println("Statistics for "+variableName);
		output.println(variableName+" count = "+count);
		output.println(variableName+" sum = "+sum);
		output.println(variableName+" minimum = "+minimum);
		output.println(variableName+" maximum = "+maximum);
		output.println(variableName+" mean = "+getMean());
		output.println(variableName+" variance = "+getVariance());
		output.println(variableName+" standard deviation = "+getStandardDeviation());
	}
}
