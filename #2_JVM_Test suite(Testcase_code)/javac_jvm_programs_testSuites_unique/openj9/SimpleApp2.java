

import java.util.regex.*;

public class SimpleApp2 {
	public void call() {
		
		boolean match = false;
		String classNameRegex = "[a-zA-Z_][[\\w]|\\$]*";
		match = Pattern.matches(classNameRegex, "SimpleApp2");
	}
}
