
package com.oti.j9.exclude;

public final class XMLStringBuffer {
		private int count;
		private boolean shared;
		char[] value;

	public XMLStringBuffer() {
		this(16);
	}

	public XMLStringBuffer(int capacity) {
		count = 0;
		shared = false;
		value = new char[capacity];
	}

	public XMLStringBuffer (String string) {
		count = string.length();
		shared = false;
		value = new char[count + 16];
		string.getChars(0, count, value, 0);
	}

	public XMLStringBuffer append (int value) {
		return append(Integer.toString(value));
	}

	public XMLStringBuffer append (long value) {
		return append(Long.toString(value));
	}

	public XMLStringBuffer append (Object value) {
		return append (String.valueOf (value));
	}


	public XMLStringBuffer append (boolean value) {
		return append (String.valueOf (value));
	}

	public XMLStringBuffer append (char value) {
		return append (String.valueOf(value));
	}

	public XMLStringBuffer append (String string) {
		if (string == null) string = String.valueOf (string);
		int adding = string.length();
		int newSize = count + adding;
		if (newSize > value.length) {
			ensureCapacityImpl(newSize);
		} else if (shared) {
			value = (char[])value.clone();
			shared = false;
		}
		string.getChars(0, adding, value, count);
		count = newSize;
		return this;
	}

	public int capacity() {
		return value.length;
	}

	public synchronized char charAt(int index) {
		try {
			if (index < count) return value[index];
		} catch (IndexOutOfBoundsException e) {}
		throw new IndexOutOfBoundsException();
	}

	public synchronized void ensureCapacity(int min) {
		if (min > value.length) ensureCapacityImpl(min);
	}

	private void ensureCapacityImpl(int min) {
		int twice = (value.length << 1) + 2;
		char[] newData = new char[min > twice ? min : twice];
		System.arraycopy(value, 0, newData, 0, count);
		value = newData;
		shared = false;
	}

	public synchronized void getChars(int start, int end, char [] buffer, int index) {
		
		try {
			if (start < count && end <= count) {
				System.arraycopy (value, start, buffer, index, end - start);
				return;
			}
		} catch(IndexOutOfBoundsException e) {}
		throw new IndexOutOfBoundsException ();
	}

	public synchronized XMLStringBuffer insert(int index, char[] chars) {
		move(chars.length, index);
		System.arraycopy(chars, 0, value, index, chars.length);
		count += chars.length;
		return this;
	}

	public synchronized XMLStringBuffer insert(int index, char ch) {
		move(1, index);
		value[index] = ch;
		count++;
		return this;
	}

	public XMLStringBuffer insert(int index, int value) {
		return insert(index, Integer.toString(value));
	}

	public XMLStringBuffer insert(int index, long value) {
		return insert(index, Long.toString(value));
	}

	public XMLStringBuffer insert(int index, Object value) {
		return insert(index, String.valueOf(value));
	}

	public synchronized XMLStringBuffer insert(int index, String string) {
		if (string == null) string = String.valueOf (string);
		int min = string.length();
		move(min, index);
		string.getChars(0, min, value, index);
		count += min;
		return this;
	}

	public XMLStringBuffer insert(int index, boolean value) {
		return insert(index, String.valueOf(value));
	}

	public int length () {
		return count;
	}

	private void move(int size, int index) {
		if (0 <= index && index <= count) {
			int newSize;
			if (value.length - count >= size) {
				if (!shared) {
					System.arraycopy(value, index, value, index + size, count - index); 
					return;
				}
				newSize = value.length;
			} else {
				int a = count + size, b = (value.length << 1) + 2;
				newSize = a > b ? a : b;
			}
			char[] newData = new char[newSize];
			System.arraycopy(value, 0, newData, 0, index);
			System.arraycopy(value, index, newData, index + size, count - index); 
			value = newData;
			shared = false;
		} else throw new IndexOutOfBoundsException();
	}



	public synchronized void setLength(int length) {
		if (length > value.length)
			ensureCapacityImpl(length);
		if (count > length) {
			if (!shared) {
				
				try {
					for (int i=length; i<count; i++) value[i] = 0;
				} catch (IndexOutOfBoundsException e) {
					throw new IndexOutOfBoundsException();
				}
			} else {
				char[] newData = new char[value.length];
				System.arraycopy(value, 0, newData, 0, length);
				value = newData;
				shared = false;
			}
		}
		count = length;
	}

	public synchronized boolean endsWith(String suffix) {

		int suffixLength = suffix.length();

		if (suffixLength < 0 || count < suffixLength)
			return false;

		int localIndex = count - suffixLength;
		int stringIndex = 0;

		while (suffixLength-- > 0) {
			if (charAt(localIndex++) != suffix.charAt(stringIndex++))
				return false;
		}
		return true;

	}

	public String toString () {
		shared = true;
		return new String (value, 0, count );
	}

}
