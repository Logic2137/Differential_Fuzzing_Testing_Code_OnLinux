import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

public class AudioFileFormatToString {

    static final int STATUS_PASSED = 0;

    static final int STATUS_FAILED = 2;

    static final int STATUS_TEMP = 95;

    public static void main(String[] argv) throws Exception {
        int testExitStatus = run(argv, System.out);
        if (testExitStatus != STATUS_PASSED) {
            throw new Exception("Test FAILED " + testExitStatus);
        }
        System.out.println("Test passed.");
    }

    public static int run(String[] argv, java.io.PrintStream out) {
        int testResult = STATUS_PASSED;
        out.println("\n==> Test for AudioFileFormat class:");
        AudioFormat testAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float) 44100.0, (int) 8, (int) 2, (int) 2, (float) 110.0, true);
        AudioFormat nullAudioFormat = null;
        AudioFileFormat.Type testAudioFileFormatType = AudioFileFormat.Type.WAVE;
        AudioFileFormat.Type nullAudioFileFormatType = null;
        AudioFileFormat testedAudioFileFormat = null;
        out.println("\n>> public AudioFileFormat constructor for AudioFileFormat.Type = null: ");
        try {
            testedAudioFileFormat = new AudioFileFormat(nullAudioFileFormatType, testAudioFormat, (int) 1024);
            out.println(">  No any Exception was thrown!");
            out.println(">  testedAudioFileFormat.getType():");
            try {
                AudioFileFormat.Type producedType = testedAudioFileFormat.getType();
                out.println(">   PASSED: producedType = " + producedType);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
            out.println(">  testedAudioFileFormat.toString():");
            try {
                String producedString = testedAudioFileFormat.toString();
                out.println(">   PASSED: producedString = " + producedString);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
        } catch (IllegalArgumentException illegArgExcept) {
            out.println(">   PASSED: expected IllegalArgumentException was thrown:");
            illegArgExcept.printStackTrace(out);
        } catch (NullPointerException nullPE) {
            out.println(">   PASSED: expected NullPointerException was thrown:");
            nullPE.printStackTrace(out);
        } catch (Throwable thrown) {
            out.println("##  FAILED: unexpected Exception was thrown:");
            thrown.printStackTrace(out);
            testResult = STATUS_FAILED;
        }
        out.println("\n>> public AudioFileFormat constructor for AudioFormat = null: ");
        try {
            testedAudioFileFormat = new AudioFileFormat(testAudioFileFormatType, nullAudioFormat, (int) 1024);
            out.println(">  No any Exception was thrown!");
            out.println(">  testedAudioFileFormat.getFormat():");
            try {
                AudioFormat producedFormat = testedAudioFileFormat.getFormat();
                out.println(">   PASSED: producedFormat = " + producedFormat);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
            out.println(">  testedAudioFileFormat.toString():");
            try {
                String producedString = testedAudioFileFormat.toString();
                out.println(">   PASSED: producedString = " + producedString);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
        } catch (IllegalArgumentException illegArgExcept) {
            out.println(">   PASSED: expected IllegalArgumentException was thrown:");
            illegArgExcept.printStackTrace(out);
        } catch (NullPointerException nullPE) {
            out.println(">   PASSED: expected NullPointerException was thrown:");
            nullPE.printStackTrace(out);
        } catch (Throwable thrown) {
            out.println("##  FAILED: unexpected Exception was thrown:");
            thrown.printStackTrace(out);
            testResult = STATUS_FAILED;
        }
        out.println("\n>> protected AudioFileFormat constructor for AudioFileFormat.Type = null: ");
        try {
            testedAudioFileFormat = new TestAudioFileFormat(nullAudioFileFormatType, (int) 1024, testAudioFormat, (int) 1024);
            out.println(">  No any Exception was thrown!");
            out.println(">  testedAudioFileFormat.getType():");
            try {
                AudioFileFormat.Type producedType = testedAudioFileFormat.getType();
                out.println(">   PASSED: producedType = " + producedType);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
            out.println(">  testedAudioFileFormat.toString():");
            try {
                String producedString = testedAudioFileFormat.toString();
                out.println(">   PASSED: producedString = " + producedString);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
        } catch (IllegalArgumentException illegArgExcept) {
            out.println(">   PASSED: expected IllegalArgumentException was thrown:");
            illegArgExcept.printStackTrace(out);
        } catch (NullPointerException nullPE) {
            out.println(">   PASSED: expected NullPointerException was thrown:");
            nullPE.printStackTrace(out);
        } catch (Throwable thrown) {
            out.println("##  FAILED: unexpected Exception was thrown:");
            thrown.printStackTrace(out);
            testResult = STATUS_FAILED;
        }
        out.println("\n>> protected AudioFileFormat constructor for AudioFormat = null: ");
        try {
            testedAudioFileFormat = new TestAudioFileFormat(testAudioFileFormatType, (int) 1024, nullAudioFormat, (int) 1024);
            out.println(">  No any Exception was thrown!");
            out.println(">  testedAudioFileFormat.getFormat():");
            try {
                AudioFormat producedFormat = testedAudioFileFormat.getFormat();
                out.println(">   PASSED: producedFormat = " + producedFormat);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
            out.println(">  testedAudioFileFormat.toString():");
            try {
                String producedString = testedAudioFileFormat.toString();
                out.println(">   PASSED: producedString = " + producedString);
            } catch (Throwable thrown) {
                out.println("##  FAILED: unexpected Exception was thrown:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
            }
        } catch (IllegalArgumentException illegArgExcept) {
            out.println(">   PASSED: expected IllegalArgumentException was thrown:");
            illegArgExcept.printStackTrace(out);
        } catch (NullPointerException nullPE) {
            out.println(">   PASSED: expected NullPointerException was thrown:");
            nullPE.printStackTrace(out);
        } catch (Throwable thrown) {
            out.println("##  FAILED: unexpected Exception was thrown:");
            thrown.printStackTrace(out);
            testResult = STATUS_FAILED;
        }
        if (testResult == STATUS_FAILED) {
            out.println("\n==> test FAILED!");
        } else {
            out.println("\n==> test PASSED!");
        }
        return testResult;
    }
}

class TestAudioFileFormat extends AudioFileFormat {

    TestAudioFileFormat(AudioFileFormat.Type type, int byteLength, AudioFormat format, int frameLength) {
        super(type, byteLength, format, frameLength);
    }
}
