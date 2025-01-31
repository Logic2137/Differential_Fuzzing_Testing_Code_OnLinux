



import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.media.sound.RIFFReader;
import com.sun.media.sound.RIFFWriter;

public class Skip {

    private static void assertEquals(Object a, Object b) throws Exception
    {
        if(!a.equals(b))
            throw new RuntimeException("assertEquals fails!");
    }

    public static void main(String[] args) throws Exception {
        test(false);
        test(true);
    }

    private static void test(boolean customStream) throws Exception {
        RIFFWriter writer = null;
        RIFFReader reader = null;
        File tempfile = File.createTempFile("test",".riff");
        try
        {
            writer = new RIFFWriter(tempfile, "TEST");
            RIFFWriter chunk = writer.writeChunk("TSCH");
            chunk.write((byte)33);
            chunk.write((byte)44);
            writer.close();
            writer = null;
            final FileInputStream fis;
            if (customStream) {
                fis = new FileInputStream(tempfile);
            } else {
                fis = new FileInputStream(tempfile) {
                    @Override
                    public long skip(long n) {
                        return 0;
                    }
                };
            }
            reader = new RIFFReader(fis);
            RIFFReader readchunk = reader.nextChunk();
            reader.skip(1);
            assertEquals(readchunk.read(), 44);
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
