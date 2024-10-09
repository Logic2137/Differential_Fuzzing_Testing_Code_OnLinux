



import java.nio.channels.*;
import java.nio.*;
import java.util.Random;

public class ScatteringRead {

    private static Random generator = new Random();

    public static void main(String[] args) throws Exception {
        scScatter();
    }

    private static void scScatter() throws Exception {
        Pipe p = Pipe.open();
        Pipe.SinkChannel sink = p.sink();
        Pipe.SourceChannel source = p.source();
        sink.configureBlocking(false);

        ByteBuffer outgoingdata = ByteBuffer.allocateDirect(30);
        byte[] someBytes = new byte[30];
        generator.nextBytes(someBytes);
        outgoingdata.put(someBytes);
        outgoingdata.flip();

        int totalWritten = 0;
        while (totalWritten < 30) {
            int written = sink.write(outgoingdata);
            if (written < 0)
                throw new Exception("Write failed");
            totalWritten += written;
        }

        ByteBuffer[] bufs = new ByteBuffer[3];
        for(int i=0; i<3; i++)
            bufs[i] = ByteBuffer.allocateDirect(10);
        long numBytesRead = source.read(bufs);
        if (numBytesRead < 30)
            throw new Exception("Pipe test failed");
        sink.close();
        source.close();
    }

}
