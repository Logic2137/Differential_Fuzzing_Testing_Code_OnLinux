
package com.oti.j9.exclude;

import java.util.Hashtable;



public interface IXMLDocumentHandler {

	public abstract void xmlStartDocument();
	public abstract void xmlEndDocument();
	public abstract void xmlStartElement(String elementName, Hashtable attributes);
	public abstract void xmlEndElement(String elementName);
	public abstract void xmlCharacters(String chars);
}
