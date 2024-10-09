package org.openj9.resources.methodparameters;





public class WithParams {

	public enum MOOD {
		HAPPY,
		SAD
	}

	public class InnerClass {
	}

	public WithParams() {
	}

	public WithParams(boolean firstParam) {
	}

	public WithParams(boolean firstParam, final int secondFinalParam) {
	}

	public WithParams(boolean firstParam, final int secondFinalParam, String thirdParam) {
	}

	public void sampleMethod0() {
	}

	public void sampleMethod1(boolean firstParam) {
	}

	public void sampleMethod2(boolean firstParam, final int secondFinalParam) {
	}

	public void sampleMethod3(boolean firstParam, final int secondFinalParam, String thirdParam) {
	}
}
