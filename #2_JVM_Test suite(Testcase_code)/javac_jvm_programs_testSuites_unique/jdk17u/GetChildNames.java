import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.ImageTypeSpecifier;

public class GetChildNames {

    public static void main(String[] argv) {
        GCNFormatImpl fmt = new GCNFormatImpl("root", 1, 10);
        fmt.addElement("cc", "root", fmt.CHILD_POLICY_EMPTY);
        String[] result = fmt.getChildNames("cc");
        if (result != null) {
            throw new RuntimeException("Failed, result is not null: " + result);
        }
    }
}

class GCNFormatImpl extends IIOMetadataFormatImpl {

    GCNFormatImpl(String root, int minChildren, int maxChildren) {
        super(root, minChildren, maxChildren);
    }

    public void addElement(String elementName, String parentName, int childPolicy) {
        super.addElement(elementName, parentName, childPolicy);
    }

    public boolean canNodeAppear(String elementName, ImageTypeSpecifier imageType) {
        return true;
    }
}
