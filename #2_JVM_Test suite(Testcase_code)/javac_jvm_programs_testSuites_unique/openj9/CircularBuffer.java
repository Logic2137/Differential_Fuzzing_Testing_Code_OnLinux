
package org.openj9.test.jsr335.defineAnonClass;

import java.lang.reflect.Array;

class CircularBuffer<T> {
	private int size;
	private int head;
	
	private T array[];
	
	public CircularBuffer(int size) {
		array = (T[]) new Object[size];
		this.size = size;
		this.head = 0;
	}
	
	public void addElement(T element) {
		array[head%size] = element;
		head++;
	}
}
