
package org.openj9.test.varhandle;

import java.nio.ByteBuffer;

abstract class ViewHelper {
	static void reset(ByteBuffer buffer) {
		buffer.put(0, (byte)1);
		buffer.put(1, (byte)2);
		buffer.put(2, (byte)3);
		buffer.put(3, (byte)4);
		buffer.put(4, (byte)5);
		buffer.put(5, (byte)6);
		buffer.put(6, (byte)7);
		buffer.put(7, (byte)8);
		buffer.put(8, (byte)9);
		buffer.put(9, (byte)10);
		buffer.put(10, (byte)11);
		buffer.put(11, (byte)12);
		buffer.put(12, (byte)13);
		buffer.put(13, (byte)14);
		buffer.put(14, (byte)15);
		buffer.put(15, (byte)16);
	}
}
