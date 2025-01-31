



import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.media.sound.RIFFReader;
import com.sun.media.sound.RIFFWriter;

public class HasNextChunk {

    private static void assertEquals(Object a, Object b) throws Exception
    {
        if(!a.equals(b))
            throw new RuntimeException("assertEquals fails!");
    }

    private static void assertTrue(boolean a) throws Exception
    {
        if(!a)
            throw new RuntimeException("assertEquals fails!");
    }

    public static void main(String[] args) throws Exception {
        RIFFWriter writer = null;
        RIFFReader reader = null;
        File tempfile = File.createTempFile("test",".riff");
        try
        {
            writer = new RIFFWriter(tempfile, "TEST");
            RIFFWriter chunk = writer.writeChunk("TSCH");
            chunk.writeByte(10);
            writer.close();
            writer = null;
            FileInputStream fis = new FileInputStream(tempfile);
            reader = new RIFFReader(fis);
            assertTrue(reader.hasNextChunk());
            RIFFReader readchunk = reader.nextChunk();
            readchunk.readByte();
            readchunk.close();
            assertTrue(!reader.hasNextChunk());
            fis.close();
            reader = null;
        }
        finally
        {
            if(writer != null)
                writer.close();
            if(reader != null)
                reader.close();
            Files.delete(Paths.get(tempfile.getAbsolutePath()));
        }
    }
}
