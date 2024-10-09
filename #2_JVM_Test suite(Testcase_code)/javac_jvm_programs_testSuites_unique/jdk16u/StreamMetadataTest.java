



import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;

import org.w3c.dom.Node;

public class StreamMetadataTest {
    protected static final String format = "GIF";

    ImageWriter writer = null;
    IIOMetadata streamData = null;
    ImageWriteParam wparam = null;
    boolean doMerge = true;

    public StreamMetadataTest() {
        writer = ImageIO.getImageWritersByFormatName(format).next();
        wparam = writer.getDefaultWriteParam();
        streamData = writer.getDefaultStreamMetadata(wparam);
    }

    public void doTest() throws IIOInvalidTreeException {
        if (streamData == null) {
            throw new RuntimeException("No stream metadata available");
        }

        String[] formatNames = streamData.getMetadataFormatNames();
        for(String fname : formatNames) {
            System.out.println("Format name: " + fname);
            Node root = streamData.getAsTree(fname);
            if (streamData.isReadOnly()) {
                throw new RuntimeException("Stream metadata is readonly!");
            }
            streamData.reset();
            streamData.mergeTree(fname, root);
        }
    }

    public static void main(String args[]) {
        StreamMetadataTest test = new StreamMetadataTest();
        try {
            test.doTest();
        } catch (Exception e) {
            throw new RuntimeException("Test failed.", e);
        }
    }
}
