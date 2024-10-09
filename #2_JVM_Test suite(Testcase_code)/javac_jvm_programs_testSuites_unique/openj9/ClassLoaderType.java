
package CustomCLs;


import java.util.*;

public final class ClassLoaderType {
	
	private String id;
	public final int ord;
	private ClassLoaderType prev;
	private ClassLoaderType next;

	private static int upperBound = 0;
	private static ClassLoaderType first = null;
	private static ClassLoaderType last = null;
	    
	private ClassLoaderType(String anID) {
		this.id = anID;
		this.ord = upperBound++;
		if (first == null) first = this;
		if (last != null) {
			this.prev = last;
			last.next = this;
		}
		last = this;
	}
	
	public static Enumeration elements() {
		return new Enumeration() {
			private ClassLoaderType curr = first;
			public boolean hasMoreElements() {
				return curr != null;
			}
			public Object nextElement() {
				ClassLoaderType c = curr;
				curr = curr.next();
				return c;
			}
		};
	}
	
	public String toString() {return this.id; }
	public static int size() { return upperBound; }
	public static ClassLoaderType first() { return first; }
	public static ClassLoaderType last()  { return last;  }
	public ClassLoaderType prev()  { return this.prev; }
	public ClassLoaderType next()  { return this.next; }

	public static final ClassLoaderType TOKEN = new
	ClassLoaderType("TokenLoader");
	public static final ClassLoaderType CACHEDURL = new
	ClassLoaderType("CachedURLLoader");
	public static final ClassLoaderType URL = new
	ClassLoaderType("URLLoader");
}
