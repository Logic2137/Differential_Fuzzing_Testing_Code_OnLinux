import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.sun.media.sound.RIFFReader;
import com.sun.media.sound.RIFFWriter;

public class ReadLong {

    private static void assertEquals(Object a, Object b) throws Exception {
        if (!a.equals(b))
            throw new RuntimeException("assertEquals fails!");
    }

    public static void main(String[] args) throws Exception {
        RIFFWriter writer = null;
        RIFFReader reader = null;
        File tempfile = File.createTempFile("test", ".riff");
        try {
            writer = new RIFFWriter(tempfile, "TEST");
            RIFFWriter chunk = writer.writeChunk("TSCH");
            chunk.writeLong(133L);
            writer.close();
            writer = null;
            FileInputStream fis = new FileInputStream(tempfile);
            reader = new RIFFReader(fis);
            assertEquals(reader.getFormat(), "RIFF");
            assertEquals(reader.getType(), "TEST");
            RIFFReader readchunk = reader.nextChunk();
            assertEquals(readchunk.getFormat(), "TSCH");
            assertEquals(reader.readLong(), 133L);
            fis.close();
            reader = null;
        } finally {
            if (writer != null)
                writer.close();
            if (reader != null)
                reader.close();
            Files.delete(Paths.get(tempfile.getAbsolutePath()));
        }
    }
}
