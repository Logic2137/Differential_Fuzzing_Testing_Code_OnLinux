package org.openj9.test.support;



public class Support_15Help {

public static CharSequence createCharSequence(final String value) {
	return new CharSequence() {
		public int length() {
			return value.length();
		}
		public char charAt(int i) {
			return value.charAt(i);
		}
		public CharSequence subSequence(int start, int end) {
			return createCharSequence(value.substring(start, end));
		}
		public String toString() {
			return value;
		}
	};
}

}
