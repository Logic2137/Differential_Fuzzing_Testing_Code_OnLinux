



import java.io.IOException;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpMultiChannel;
import com.sun.nio.sctp.SctpServerChannel;
import static java.lang.System.out;


public class SctpSanity {

    public static void main(String... args) throws IOException {
        switch (Integer.valueOf(args[0])) {
            case 1: testSctpChannel();        break;
            case 2: testSctpServerChannel();  break;
            case 3: testSctpMultiChannel();   break;
            default: throw new AssertionError("should not reach here");
        }
    }

    static void testSctpChannel() throws IOException {
        try (SctpChannel channel = SctpChannel.open()) {
            out.println("created SctpChannel:" + channel);
        } catch (UnsupportedOperationException uoe) {
            
            out.println("ok, caught:" + uoe);
        }
    }

    static void testSctpServerChannel() throws IOException {
        try (SctpServerChannel channel = SctpServerChannel.open()) {
            out.println("created SctpServerChannel:" + channel);
        } catch (UnsupportedOperationException uoe) {
            
            out.println("ok, caught:" + uoe);
        }
    }

    static void testSctpMultiChannel() throws IOException {
        try (SctpMultiChannel channel = SctpMultiChannel.open()) {
            out.println("created SctpMultiChannel:" + channel);
        } catch (UnsupportedOperationException uoe) {
            
            out.println("ok, caught:" + uoe);
        }
    }
}

