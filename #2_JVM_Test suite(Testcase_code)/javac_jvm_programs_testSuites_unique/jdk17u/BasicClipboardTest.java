import java.awt.datatransfer.*;

public class BasicClipboardTest implements ClipboardOwner {

    StringSelection strSelect = new StringSelection("Transferable String Selection");

    StringSelection strCheck;

    String clipName = "Test Clipboard";

    Clipboard clip = new Clipboard(clipName);

    DataFlavor dataFlavor, testDataFlavor;

    DataFlavor[] dataFlavorArray;

    Object testObject;

    String strTest = null;

    public static void main(String[] args) throws Exception {
        new BasicClipboardTest().doTest();
    }

    public void doTest() throws Exception {
        dataFlavor = new DataFlavor(DataFlavor.javaRemoteObjectMimeType, null, this.getClass().getClassLoader());
        testDataFlavor = DataFlavor.selectBestTextFlavor(dataFlavorArray);
        if (testDataFlavor != null)
            throw new RuntimeException("\n***Error in selectBestTextFlavor");
        dataFlavorArray = new DataFlavor[0];
        testDataFlavor = DataFlavor.selectBestTextFlavor(dataFlavorArray);
        if (testDataFlavor != null)
            throw new RuntimeException("\n***Error in selectBestTextFlavor");
        dataFlavorArray = new DataFlavor[1];
        dataFlavorArray[0] = new DataFlavor(DataFlavor.javaSerializedObjectMimeType + ";class=java.io.Serializable");
        testDataFlavor = DataFlavor.selectBestTextFlavor(dataFlavorArray);
        if (testDataFlavor != null)
            throw new RuntimeException("\n***Error in selectBestTextFlavor");
        if (clip.getName() != clipName)
            throw new RuntimeException("\n*** Error in Clipboard.getName()");
        clip.setContents(null, null);
        clip.setContents(null, new BasicClipboardTest());
        clip.setContents(null, this);
        clip.setContents(strSelect, this);
        strCheck = (StringSelection) clip.getContents(this);
        if (!strCheck.equals(strSelect))
            throw new RuntimeException("\n***The contents of the clipboard are " + "not the same as those that were set");
        dataFlavor = DataFlavor.stringFlavor;
        strSelect = new StringSelection(null);
        try {
            testObject = dataFlavor.getReaderForText(strSelect);
            throw new RuntimeException("\n***Error in getReaderForText. An IAE should have been thrown");
        } catch (IllegalArgumentException iae) {
        }
        dataFlavor.setHumanPresentableName("String Flavor");
        if (!(dataFlavor.getParameter("humanPresentableName")).equals("String Flavor"))
            throw new RuntimeException("\n***Error in getParameter");
        try {
            if (dataFlavor.isMimeTypeEqual(strTest))
                throw new RuntimeException("\n***Error in DataFlavor.equals(String s)");
        } catch (NullPointerException e) {
        }
        if (!(dataFlavor.isMimeTypeEqual(dataFlavor.getMimeType())))
            throw new RuntimeException("\n***Error in DataFlavor.equals(String s)");
        if (!dataFlavorArray[0].isMimeTypeSerializedObject())
            throw new RuntimeException("\n***Error in isMimeTypeSerializedObject()");
        System.out.println(dataFlavorArray[0].getDefaultRepresentationClass());
        System.out.println(dataFlavorArray[0].getDefaultRepresentationClassAsString());
        if (dataFlavor.isFlavorRemoteObjectType())
            System.out.println("The DataFlavor is a remote object type");
        testDataFlavor = (DataFlavor) dataFlavor.clone();
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
