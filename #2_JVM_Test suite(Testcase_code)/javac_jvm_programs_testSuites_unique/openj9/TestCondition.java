

abstract class TestCondition {
	static final int SUCCESS = 0;
	static final int FAILURE = 1;
	static final int REQUIRED = 2;

	protected static int parseType( String s ) {
		if (s == null || s.equalsIgnoreCase("success")) {
			return SUCCESS;
		} else if (s.equalsIgnoreCase("failure")) {
			return FAILURE;
		} else if (s.equalsIgnoreCase("required")) {
			return REQUIRED;
		}
		return SUCCESS;
	}
	
	abstract int getType();
	abstract boolean match( Object o );
	public abstract String toString();
}
