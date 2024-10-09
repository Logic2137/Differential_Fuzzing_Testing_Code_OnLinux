
package org.openj9.test.varhandle;

abstract class ConstantsHelper {
	static final char FIRST_CHAR_LE = 
			(1 << 0) + 
			(2 << 8);
	static final char CHANGED_CHAR_LE = 
			(10 << 0) + 
			(20 << 8);
	static final char LAST_CHAR_LE = 
			(15 << 0) + 
			(16 << 8);
	static final int FIRST_INT_LE = 
			(1 << 0) + 
			(2 << 8) + 
			(3 << 16) + 
			(4 << 24);
	static final int CHANGED_INT_LE = 
			(10 << 0) + 
			(20 << 8) + 
			(30 << 16) + 
			(40 << 24);
	static final int LAST_INT_LE = 
			(13 << 0) + 
			(14 << 8) + 
			(15 << 16) + 
			(16 << 24);
	static final long FIRST_LONG_LE = 
			(1L << 0) + 
			(2L << 8) + 
			(3L << 16) + 
			(4L << 24) + 
			(5L << 32) + 
			(6L << 40) + 
			(7L << 48) + 
			(8L << 56);
	static final long CHANGED_LONG_LE = 
			(10L << 0) + 
			(20L << 8) + 
			(30L << 16) + 
			(40L << 24) + 
			(50L << 32) + 
			(60L << 40) + 
			(70L << 48) + 
			(80L << 56);
	static final long LAST_LONG_LE = 
			(9L << 0) + 
			(10L << 8) + 
			(11L << 16) + 
			(12L << 24) + 
			(13L << 32) + 
			(14L << 40) + 
			(15L << 48) + 
			(16L << 56);
	static final double FIRST_DOUBLE_LE = Double.longBitsToDouble(FIRST_LONG_LE);
	static final double CHANGED_DOUBLE_LE = Double.longBitsToDouble(CHANGED_LONG_LE);
	static final double LAST_DOUBLE_LE = Double.longBitsToDouble(LAST_LONG_LE);
	static final float FIRST_FLOAT_LE = Float.intBitsToFloat(FIRST_INT_LE);
	static final float CHANGED_FLOAT_LE = Float.intBitsToFloat(CHANGED_INT_LE);
	static final float LAST_FLOAT_LE = Float.intBitsToFloat(LAST_INT_LE);
	static final short FIRST_SHORT_LE = (short)FIRST_CHAR_LE;
	static final short CHANGED_SHORT_LE = (short)CHANGED_CHAR_LE;
	static final short LAST_SHORT_LE = (short)LAST_CHAR_LE;
	static final int INITIAL_INT_AT_INDEX_3_LE = 
			(4 << 0) + 
			(5 << 8) + 
			(6 << 16) + 
			(7 << 24);
	static final int INITIAL_INT_AT_INDEX_4_LE = 
			(5 << 0) + 
			(6 << 8) + 
			(7 << 16) + 
			(8 << 24);
	static final short INITIAL_SHORT_AT_INDEX_1_LE = 
			(2 << 0) + 
			(3 << 8);

	static final char FIRST_CHAR_BE = Character.reverseBytes(FIRST_CHAR_LE);
	static final char CHANGED_CHAR_BE = Character.reverseBytes(CHANGED_CHAR_LE);
	static final char LAST_CHAR_BE = Character.reverseBytes(LAST_CHAR_LE);
	static final double FIRST_DOUBLE_BE = Double.longBitsToDouble(Long.reverseBytes(Double.doubleToRawLongBits(FIRST_DOUBLE_LE)));
	static final double CHANGED_DOUBLE_BE = Double.longBitsToDouble(Long.reverseBytes(Double.doubleToRawLongBits(CHANGED_DOUBLE_LE)));
	static final double LAST_DOUBLE_BE = Double.longBitsToDouble(Long.reverseBytes(Double.doubleToRawLongBits(LAST_DOUBLE_LE)));
	static final float FIRST_FLOAT_BE = Float.intBitsToFloat(Integer.reverseBytes(Float.floatToRawIntBits(FIRST_FLOAT_LE)));
	static final float CHANGED_FLOAT_BE = Float.intBitsToFloat(Integer.reverseBytes(Float.floatToRawIntBits(CHANGED_FLOAT_LE)));
	static final float LAST_FLOAT_BE = Float.intBitsToFloat(Integer.reverseBytes(Float.floatToRawIntBits(LAST_FLOAT_LE)));
	static final int FIRST_INT_BE = Integer.reverseBytes(FIRST_INT_LE);
	static final int CHANGED_INT_BE = Integer.reverseBytes(CHANGED_INT_LE);
	static final int LAST_INT_BE = Integer.reverseBytes(LAST_INT_LE);
	static final long FIRST_LONG_BE = Long.reverseBytes(FIRST_LONG_LE);
	static final long CHANGED_LONG_BE = Long.reverseBytes(CHANGED_LONG_LE);
	static final long LAST_LONG_BE = Long.reverseBytes(LAST_LONG_LE);
	static final short FIRST_SHORT_BE = Short.reverseBytes(FIRST_SHORT_LE);
	static final short CHANGED_SHORT_BE = Short.reverseBytes(CHANGED_SHORT_LE);
	static final short LAST_SHORT_BE = Short.reverseBytes(LAST_SHORT_LE);
	static final int INITIAL_INT_AT_INDEX_3_BE = Integer.reverseBytes(INITIAL_INT_AT_INDEX_3_LE);
	static final int INITIAL_INT_AT_INDEX_4_BE = Integer.reverseBytes(INITIAL_INT_AT_INDEX_4_LE);
	static final short INITIAL_SHORT_AT_INDEX_1_BE = Short.reverseBytes(INITIAL_SHORT_AT_INDEX_1_LE);
}
