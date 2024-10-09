
package examples;


public interface CrossPackageDefaultMethodInterface {

	default int addDefault(int a, int b) {
		return (a + b);
	}
	
}
