import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataFormat;
import javax.imageio.metadata.IIOMetadataFormatImpl;

class MyIIOMetadataFormatImpl extends IIOMetadataFormatImpl {

    MyIIOMetadataFormatImpl() {
        super("root", CHILD_POLICY_EMPTY);
        addObjectValue("root", byte.class, 123, 321);
    }

    public boolean canNodeAppear(String nodeName, ImageTypeSpecifier type) {
        return true;
    }
}

public class ObjectArrayMaxLength {

    public static void main(String[] args) {
        IIOMetadataFormat f = new MyIIOMetadataFormatImpl();
        if (f.getObjectArrayMaxLength("root") != 321) {
            throw new RuntimeException("Bad value for getObjectArrayMaxLength!");
        }
    }
}
