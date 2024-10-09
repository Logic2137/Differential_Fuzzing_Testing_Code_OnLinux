



import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import javax.imageio.metadata.*;

import javax.imageio.stream.*;
import javax.imageio.plugins.tiff.*;


public class ReadUnknownTagsTest {

    private final static int SZ = 50;
    private final static Color C = Color.RED;

    private final static int DESCRIPTION_TAG =
        BaselineTIFFTagSet.TAG_IMAGE_DESCRIPTION;
    private final static String DESCRIPTION = "A Test Image";

    private final static int FAX_TAG = FaxTIFFTagSet.TAG_CLEAN_FAX_DATA;
    private final static short FAX_DATA =
        FaxTIFFTagSet.CLEAN_FAX_DATA_ERRORS_UNCORRECTED;

    private final boolean ignoreMetadata;
    private final boolean readUnknownTags;

    public ReadUnknownTagsTest(boolean ignoreMetadata,
        boolean readUnknownTags) {
        this.ignoreMetadata = ignoreMetadata;
        this.readUnknownTags = readUnknownTags;
    }

    private ImageWriter getTIFFWriter() {

        java.util.Iterator<ImageWriter> writers =
            ImageIO.getImageWritersByFormatName("TIFF");
        if (!writers.hasNext()) {
            throw new RuntimeException("No writers available for TIFF format");
        }
        return writers.next();
    }

    private ImageReader getTIFFReader() {

        java.util.Iterator<ImageReader> readers =
            ImageIO.getImageReadersByFormatName("TIFF");
        if (!readers.hasNext()) {
            throw new RuntimeException("No readers available for TIFF format");
        }
        return readers.next();
    }


    private void writeImage() throws Exception {

        String fn = "test-" + ignoreMetadata + ".tiff";
        OutputStream s = new BufferedOutputStream(new FileOutputStream(fn));
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(s)) {

            ImageWriter writer = getTIFFWriter();
            writer.setOutput(ios);

            BufferedImage img = new BufferedImage(SZ, SZ,
                BufferedImage.TYPE_INT_RGB);
            Graphics g = img.getGraphics();
            g.setColor(C);
            g.fillRect(0, 0, SZ, SZ);
            g.dispose();

            ImageWriteParam param = writer.getDefaultWriteParam();

            IIOMetadata md = writer.getDefaultImageMetadata(
                    new ImageTypeSpecifier(img), param);

            TIFFDirectory dir = TIFFDirectory.createFromMetadata(md);

            TIFFTag descTag =
                BaselineTIFFTagSet.getInstance().getTag(DESCRIPTION_TAG);
            dir.addTIFFField(new TIFFField(descTag, TIFFTag.TIFF_ASCII, 1,
                new String[] {DESCRIPTION}));

            TIFFTag faxTag = FaxTIFFTagSet.getInstance().getTag(FAX_TAG);
            dir.addTIFFField(new TIFFField(faxTag, FAX_DATA));

            writer.write(new IIOImage(img, null, dir.getAsMetadata()));

            ios.flush();
            writer.dispose();
        }
        s.close();
    }

    private void readAndCheckImage() throws Exception {

        ImageReader reader = getTIFFReader();

        String fn = "test-" + ignoreMetadata + ".tiff";
        ImageInputStream s = ImageIO.createImageInputStream(new File(fn));

        reader.setInput(s, false, ignoreMetadata);

        int ni = reader.getNumImages(true);
        check(ni == 1, "invalid number of images");


        TIFFImageReadParam param = new TIFFImageReadParam();
        
        param.removeAllowedTagSet(FaxTIFFTagSet.getInstance());

        
        if (param.getReadUnknownTags()) {
            throw new RuntimeException("Default readUnknownTags is not false");
        }
        param.setReadUnknownTags(readUnknownTags);
        if (param.getReadUnknownTags() != readUnknownTags) {
            throw new RuntimeException("Incorrect readUnknownTags setting "
                + "\"" + readUnknownTags + "\"");
        }

        
        IIOImage i = reader.readAll(0, param);
        BufferedImage bi = (BufferedImage) i.getRenderedImage();

        check(bi.getWidth()  == SZ, "invalid width");
        check(bi.getHeight() == SZ, "invalid height");
        Color c = new Color(bi.getRGB(SZ / 2, SZ / 2));
        check(c.equals(C), "invalid color");

        IIOMetadata metadata = i.getMetadata();

        
        
        
        if (metadata == null) {
            throw new RuntimeException("No image metadata retrieved");
        }

        TIFFDirectory dir = TIFFDirectory.createFromMetadata(metadata);

        
        
        
        
        int failures = 0;
        if (!dir.containsTIFFField(BaselineTIFFTagSet.TAG_IMAGE_WIDTH)) {
            System.err.println("Metadata is missing essential ImageWidth tag");
            failures++;
        } else {
            TIFFField widthField =
                dir.getTIFFField(BaselineTIFFTagSet.TAG_IMAGE_WIDTH);
            System.out.printf("ImageWidth: %d%n", widthField.getAsLong(0));
        }

        
        
        
        
        boolean hasDescription = dir.containsTIFFField(DESCRIPTION_TAG);
        System.out.println("ImageDescription (" + !ignoreMetadata + "): "
            + hasDescription);
        if (ignoreMetadata && hasDescription) {
            System.err.println
                ("Description metadata present despite ignoreMetadata");
            failures++;
        } else if (!ignoreMetadata && !hasDescription) {
            System.err.println
                ("Description metadata absent despite !ignoreMetadata");
            failures++;
        }

        
        
        
        
        boolean shouldHaveFaxField = !ignoreMetadata && readUnknownTags;
        boolean hasFaxField = dir.containsTIFFField(FAX_TAG);
        System.out.println("CleanFaxData (" + shouldHaveFaxField + "): "
            + hasFaxField);

        if (ignoreMetadata) {
            if (hasFaxField) {
                System.err.println
                    ("Fax metadata present despite ignoreMetadata");
                failures++;
            }
        } else { 
            if (!readUnknownTags && hasFaxField) {
                System.err.println
                    ("Fax metadata present despite !readUnknownTags");
                failures++;
            } else if (readUnknownTags && !hasFaxField) {
                System.err.println
                    ("Fax metadata absent despite readUnknownTags");
                failures++;
            }
        }

        if (failures > 0) {
            throw new RuntimeException("Test failed for ignoreMetadata "
                + ignoreMetadata + " and readUnknownTags " + readUnknownTags);
        }
    }

    public void run() {
        try {
            writeImage();
            readAndCheckImage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void check(boolean ok, String msg) {
        if (!ok) { throw new RuntimeException(msg); }
    }

    public static void main(String[] args) {
        int failures = 0;

        System.out.println();
        for (boolean ignoreMetadata : new boolean[] {false, true}) {
            for (boolean readUnknownTags : new boolean[] {false, true}) {
                try {
                    System.out.printf
                        ("ignoreMetadata: %s, readUnknownTags: %s%n",
                        ignoreMetadata, readUnknownTags);
                    (new ReadUnknownTagsTest(ignoreMetadata,
                        readUnknownTags)).run();
                } catch (Exception e) {
                    e.printStackTrace();
                    failures++;
                } finally {
                    System.out.println();
                }
            }
        }
    }
}
