

import javax.sound.sampled.AudioPermission;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class GetLine {

    static boolean isSoundAccessDenied = false;
    static {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkPermission(new AudioPermission("*"));
            } catch (SecurityException e) {
                isSoundAccessDenied = true;
            }
        }
    }

    static final int STATUS_PASSED = 0;
    static final int STATUS_FAILED = 2;
    static final int STATUS_TEMP = 95;
    static java.io.PrintStream log = System.err;

    public static void main(String argv[]) throws Exception {
        if (run(argv, System.out) == STATUS_FAILED) {
            throw new Exception("Test FAILED");
        }
        System.out.println("Test passed.");
    }

    public static int run(String argv[], java.io.PrintStream out) {
        String testCaseID = "LineListener2001";

        log.println("===== " + testCaseID + " =====");

        boolean failed = false;
        Line l = null;



        

        DataLine.Info s_info = new DataLine.Info(SourceDataLine.class, null);
        Line.Info infos[] = AudioSystem.getSourceLineInfo( s_info );

        if( infos.length < 1 ) {
            log.println("Line.Info array == 0");
            return STATUS_PASSED;
        }
        try {
            l = AudioSystem.getLine(infos[0]);
        } catch(SecurityException lue) {
            log.println("SecurityException");
            return STATUS_PASSED;
        } catch (LineUnavailableException e1) {
            log.println("LUE");
            return STATUS_PASSED;
        } catch (IllegalArgumentException iae) {
            log.println("IllegalArgumentException should not be thrown "
                     + "for supported line");
            iae.printStackTrace(log);
            return STATUS_FAILED;
        }
        out.println("Passed.");
        return STATUS_PASSED;
    }

}
