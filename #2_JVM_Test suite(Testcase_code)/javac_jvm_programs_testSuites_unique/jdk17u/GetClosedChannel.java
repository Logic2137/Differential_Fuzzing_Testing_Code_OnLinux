import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;

public class GetClosedChannel {

    private static final int NUM_CHANNELS = 3;

    private static final int NUM_EXCEPTIONS = 2 * NUM_CHANNELS;

    public static void main(String[] args) throws IOException {
        int exceptions = 0;
        int openChannels = 0;
        for (int i = 0; i < NUM_CHANNELS; i++) {
            File f = File.createTempFile("fcbug", ".tmp");
            f.deleteOnExit();
            FileChannel fc = null;
            boolean shared = false;
            switch(i) {
                case 0:
                    System.out.print("FileInputStream...");
                    FileInputStream fis = new FileInputStream(f);
                    fis.close();
                    fc = fis.getChannel();
                    if (fc.isOpen()) {
                        System.err.println("FileInputStream channel should not be open");
                        openChannels++;
                    }
                    shared = true;
                    break;
                case 1:
                    System.out.print("FileOutputStream...");
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.close();
                    fc = fos.getChannel();
                    if (fc.isOpen()) {
                        System.err.println("FileOutputStream channel should not be open");
                        openChannels++;
                    }
                    break;
                case 2:
                    System.out.print("RandomAccessFile...");
                    RandomAccessFile raf = new RandomAccessFile(f, "rw");
                    raf.close();
                    fc = raf.getChannel();
                    if (fc.isOpen()) {
                        System.err.println("RandomAccessFile channel should not be open");
                        openChannels++;
                    }
                    break;
                default:
                    assert false : "Should not get here";
            }
            try {
                long position = fc.position();
                System.err.println("Channel " + i + " position is " + position);
            } catch (ClosedChannelException cce) {
                exceptions++;
            }
            try {
                fc.tryLock(0, Long.MAX_VALUE, shared);
            } catch (ClosedChannelException e) {
                System.out.println("OK");
                exceptions++;
            } catch (Error err) {
                System.err.println(err);
            }
        }
        if (exceptions != NUM_EXCEPTIONS || openChannels != 0) {
            throw new RuntimeException("FAILED:" + " ClosedChannelExceptions: expected: " + NUM_EXCEPTIONS + " actual: " + exceptions + ";" + System.lineSeparator() + " number of open channels: expected: 0 " + " actual: " + openChannels + ".");
        }
    }
}
