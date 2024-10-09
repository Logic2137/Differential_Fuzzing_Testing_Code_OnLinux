package j9vm.test.romclasscreation;




public class ROMRetentionAfterRAMFailureTest {
	
	public static final String CLASS_TO_LOAD = "ParentlessChild";
	public static final int ATTEMPTS = 10;

	public static void main(String[] args) {
		new ROMRetentionAfterRAMFailureTest().test();
	}

	private void test() {
		for (int i = 0; i < ATTEMPTS; i++) {
			try {
				Class.forName(CLASS_TO_LOAD);
				System.err.println("Error: Class.forName(" + CLASS_TO_LOAD + ") should not have succeeded!");
			} catch (java.lang.ClassNotFoundException exc) {
			} catch (java.lang.NoClassDefFoundError err) {
			}
		}
	}
}
