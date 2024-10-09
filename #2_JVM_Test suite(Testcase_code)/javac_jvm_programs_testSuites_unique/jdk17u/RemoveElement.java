import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataFormat;
import javax.imageio.ImageTypeSpecifier;

public class RemoveElement {

    public static void main(String[] args) {
        String elem = "elem2";
        int policy = IIOMetadataFormat.CHILD_POLICY_SOME;
        MyFormatImpl fmt = new MyFormatImpl("root", 1, 10);
        fmt.addElement("elem1", "root", policy);
        fmt.addElement(elem, "root", policy);
        fmt.removeElement("elem1");
        boolean gotIAE = false;
        try {
            fmt.getChildPolicy("elem1");
        } catch (IllegalArgumentException e) {
            gotIAE = true;
        }
        if (!gotIAE) {
            throw new RuntimeException("Element is still present!");
        }
        String[] chNames = fmt.getChildNames("root");
        if (chNames.length != 1) {
            throw new RuntimeException("Root still has more than 1 child!");
        }
        if (!elem.equals(chNames[0])) {
            throw new RuntimeException("Root's remaining child is incorrect!");
        }
    }

    static class MyFormatImpl extends IIOMetadataFormatImpl {

        MyFormatImpl(String root, int minChildren, int maxChildren) {
            super(root, minChildren, maxChildren);
        }

        public void addElement(String elementName, String parentName, int childPolicy) {
            super.addElement(elementName, parentName, childPolicy);
        }

        public void removeElement(String elementName) {
            super.removeElement(elementName);
        }

        public boolean canNodeAppear(String elementName, ImageTypeSpecifier imageType) {
            return true;
        }
    }
}
