
package com.oti.j9.exclude;


public class XMLException extends Exception {

	Exception _fCause;

	public XMLException()                {}
	public XMLException(String detail)   {super(detail);}
	public XMLException(Exception cause) {_fCause = cause;}
}
