
package org.w3c.dom;

public interface Node {

    public short getNodeType();

    public org.w3c.dom.Document getOwnerDocument();

    public Node getFirstChild();

    public String getPrefix();

    public String getLocalName();

    public boolean hasAttributes();
}
