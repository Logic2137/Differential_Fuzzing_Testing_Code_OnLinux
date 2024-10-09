

package transform;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DocumentExtFunc {

    public static String test(NodeList list) {
        Node node = list.item(0);
        return "["+node.getNodeName() + ":" + node.getTextContent()+"]";
    }
}
