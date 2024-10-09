import org.w3c.dom.Node;

public class TestFunc {

    public static String test(Node node) {
        String textContent = node.getTextContent();
        String nodeValue = node.getNodeValue();
        return textContent + ":" + nodeValue;
    }
}
