
package j9vm.test.unsafe;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class UnsafeObjectGetTest {
	static Unsafe myUnsafe;

	private Object objectField = "aString";
	private long longField = 0x0102030405060708L;
	private int intField = 0x01020304;
	private int shortField = 0x0102;
	private int byteField = 0x01;
	
	
	static Unsafe getUnsafeInstance() throws IllegalAccessException {
		
		Field[] staticFields = Unsafe.class.getDeclaredFields();
		for (Field field : staticFields) {
			if (field.getType() == Unsafe.class) {
		 		field.setAccessible(true);
		 		return (Unsafe)field.get(Unsafe.class);
			}
		}
		throw new Error("Unable to find an instance of Unsafe");
	}
	
	public void testGetByte() throws SecurityException, NoSuchFieldException {
		Field myField = this.getClass().getDeclaredField("byteField");
		long myFieldOffset = myUnsafe.objectFieldOffset(myField);
		byte value = myUnsafe.getByte(this, myFieldOffset);
		if (value != byteField) {
			throw new Error("Expected " + byteField + " from getByte() but got " + value);
		}
		byte volatileValue = myUnsafe.getByteVolatile(this, myFieldOffset);
		if (volatileValue != byteField) {
			throw new Error("Expected " + byteField + " from getByteVolatile() but got " + volatileValue);
		}
	}
	
	public void testGetShort() throws SecurityException, NoSuchFieldException {
		Field myField = this.getClass().getDeclaredField("shortField");
		long myFieldOffset = myUnsafe.objectFieldOffset(myField);
		short value = myUnsafe.getShort(this, myFieldOffset);
		if (value != shortField) {
			throw new Error("Expected " + shortField + " from getShort() but got " + value);
		}
		short volatileValue = myUnsafe.getShortVolatile(this, myFieldOffset);
		if (volatileValue != shortField) {
			throw new Error("Expected " + shortField + " from getShortVolatile() but got " + volatileValue);
		}
	}

	public void testGetInt() throws SecurityException, NoSuchFieldException {
		Field myField = this.getClass().getDeclaredField("intField");
		long myFieldOffset = myUnsafe.objectFieldOffset(myField);
		int value = myUnsafe.getInt(this, myFieldOffset);
		if (value != intField) {
			throw new Error("Expected " + intField + " from getInt() but got " + value);
		}
		int volatileValue = myUnsafe.getIntVolatile(this, myFieldOffset);
		if (volatileValue != intField) {
			throw new Error("Expected " + intField + " from getIntVolatile() but got " + volatileValue);
		}
	}
	
	public void testGetLong() throws SecurityException, NoSuchFieldException {
		Field myField = this.getClass().getDeclaredField("longField");
		long myFieldOffset = myUnsafe.objectFieldOffset(myField);
		long value = myUnsafe.getLong(this, myFieldOffset);
		if (value != longField) {
			throw new Error("Expected " + longField + " from getInt() but got " + value);
		}
		long volatileValue = myUnsafe.getLongVolatile(this, myFieldOffset);
		if (volatileValue != longField) {
			throw new Error("Expected " + longField + " from getLongVolatile() but got " + volatileValue);
		}
	}
	
	public void testGetObject() throws SecurityException, NoSuchFieldException {
		Field myField = this.getClass().getDeclaredField("objectField");
		long myFieldOffset = myUnsafe.objectFieldOffset(myField);
		Object value = myUnsafe.getObject(this, myFieldOffset);
		if (value != objectField) {
			throw new Error("Expected " + objectField + " from getInt() but got something else");
		}
		Object volatileValue = myUnsafe.getObjectVolatile(this, myFieldOffset);
		if (volatileValue != objectField) {
			throw new Error("Expected " + objectField + " from getObjectVolatile() but got something else");
		}
	}

	public static void main(String[] args) throws Exception {
		myUnsafe = getUnsafeInstance();
		UnsafeObjectGetTest tester = new UnsafeObjectGetTest();
		tester.testGetLong();
		tester.testGetInt();
		tester.testGetShort();
		tester.testGetByte();
		tester.testGetObject();
	}
	
}
