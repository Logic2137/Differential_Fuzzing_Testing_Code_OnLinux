
package j9vm.runner;
import java.util.*;

public class MenuItem {
	String displayString;
	Vector classNames;

public MenuItem(String displayString, Vector classNames)  {
	super();
	this.displayString = displayString;
	this.classNames = classNames;
}

public MenuItem(String displayString, String className)  {
	super();
	this.displayString = displayString;
	this.classNames = new Vector(1);
	this.classNames.addElement(className);
}

public String getDisplayString() { return displayString; }
public void setDisplayString(String newValue) {	displayString = newValue; }
public Vector getClassNames() {	return classNames; }
public void setClassNames(Vector newValue) { classNames = newValue; }

}
