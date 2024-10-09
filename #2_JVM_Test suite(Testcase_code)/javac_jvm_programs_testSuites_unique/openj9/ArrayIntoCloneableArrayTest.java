
package j9vm.test.arraystore;


public class ArrayIntoCloneableArrayTest {
	public static void main(String[] args) {
		System.out.println("Testing for ArrayStoreException...");
		try {
			Cloneable[] cloneableArray = new Cloneable[10];
			Object[] objectArray = new Object[10];
			cloneableArray[0] = objectArray;
		} catch (ArrayStoreException e) {
			System.out.println("***FAILED***");
			throw e;
		}
	}
}
