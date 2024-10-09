package com.ibm.j9.cfdump.tests.lineardump.utils;




public class Field {
	private String name;
	private String value;
	private String addressStart;
	private String addressEnd;
	private String extraDescription;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddressStart() {
		return addressStart;
	}
	public void setAddressStart(String addressStart) {
		this.addressStart = addressStart;
	}
	public String getAddressEnd() {
		return addressEnd;
	}
	public void setAddressEnd(String addressEnd) {
		this.addressEnd = addressEnd;
	}
	public String getExtraDescription() {
		return extraDescription;
	}
	public void setExtraDescription(String extraDescription) {
		this.extraDescription = extraDescription;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String toString() {
		return String.format("%s-%s [ %s %s ] %s", addressStart, addressEnd, value, name, extraDescription);
	}
}
