
package org.openj9.test.util;

import java.util.ArrayList;

public class ExcludeData {
	private String methodsToExclude;
	private String className;
	private String defectNumber;
	private ArrayList<String> excludeGroupNames;
	
	public ExcludeData(String methodsToExclude, String className, String defectNumber, ArrayList<String> excludeGroupNames) {
		this.methodsToExclude = methodsToExclude;
		this.className = className;
		this.defectNumber = defectNumber;
		this.excludeGroupNames = new ArrayList<String> (excludeGroupNames);
	}
	
	public String getMethodsToExclude() { return methodsToExclude;}
	public String getClassName() { return className;}
	public String getDefectNumber() { return defectNumber;}
	public ArrayList<String> getExcludeGroupNames() { return excludeGroupNames;}
	
	public void setMethodsToExclude(String methodsToExclude) { this.methodsToExclude = methodsToExclude; }
	public void setClassName(String className) { this.className = className; }
	public void setDefectNumber(String defectNumber) { this.defectNumber = defectNumber; }
	public void setMethodsToExclude(ArrayList<String> excludeGroupNames) {this.excludeGroupNames = excludeGroupNames; }
	
}
