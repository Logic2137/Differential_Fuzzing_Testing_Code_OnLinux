

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;


public class SDLwrite {

    static final int STATUS_PASSED = 0;
    static final int STATUS_FAILED = 2;

    public static void main(String argv[]) throws Exception {
        int testExitStatus = run(argv, System.out);
        if (testExitStatus != STATUS_PASSED) {
            throw new Exception("test FAILED!");
        }
    }

    public static int run(String argv[], java.io.PrintStream out) {
        int testResult = STATUS_PASSED;

        out.println
            ("\n==> Test for SourceDataLine.write() method for not open and not started line:");

        Mixer.Info[] installedMixersInfo = AudioSystem.getMixerInfo();

        if ( installedMixersInfo == null ) {
            out.println("## AudioSystem.getMixerInfo() returned unexpected result:");
            out.println("#  expected: an array of Mixer.Info objects (may be array of length 0);");
            out.println("#  produced: null;");
            return STATUS_FAILED;
        }

        if ( installedMixersInfo.length == 0 ) {
            
            return STATUS_PASSED;
        }

        Mixer testedMixer = null;
        for (int i=0; i < installedMixersInfo.length; i++) {
            try {
                testedMixer = AudioSystem.getMixer(installedMixersInfo[i]);
            } catch (SecurityException securityException) {
                
                continue;
            } catch (Throwable thrown) {
                out.println("## AudioSystem.getMixer() threw unexpected exception:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
                continue;
            }

            try {
                testedMixer.open();
            } catch (LineUnavailableException lineUnavailableException) {
                
                continue;
            } catch (SecurityException securityException) {
                
                continue;
            } catch (Throwable thrown) {
                out.println("## Mixer.open() threw unexpected exception:");
                out.println("#  Mixer = " + testedMixer);
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
                continue;
            }
            Line.Info supportedSourceLineInfo[] = null;
            try {
                supportedSourceLineInfo = testedMixer.getSourceLineInfo();
            } catch (Throwable thrown) {
                out.println("## Mixer.getSourceLineInfo() threw unexpected exception:");
                out.println("#  Mixer = " + testedMixer);
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
                testedMixer.close();
                continue;
            }
            if ( supportedSourceLineInfo == null ) {
                out.println("## Mixer.getSourceLineInfo() returned null array");
                out.println("#  Mixer = " + testedMixer);
                testResult = STATUS_FAILED;
                testedMixer.close();
                continue;
            }
            out.println("\n>>>  testedMixer["+i+"] = " + testedMixer);
            out.println("\n>>  supportedSourceLineInfo.length = " + supportedSourceLineInfo.length);

            for (int j=0; j < supportedSourceLineInfo.length; j++) {
                Line.Info testSourceLineInfo = supportedSourceLineInfo[j];

                Line testSourceLine = null;
                try {
                    testSourceLine = testedMixer.getLine(testSourceLineInfo);
                } catch (LineUnavailableException lineUnavailableException) {
                    
                    continue;
                } catch (SecurityException securityException) {
                    
                    continue;
                } catch (Throwable thrown) {
                    out.println("## Mixer.getLine(Line.Info) threw unexpected Exception:");
                    out.println("#  Mixer = " + testedMixer);
                    out.println("#  Line.Info = " + testSourceLineInfo);
                    thrown.printStackTrace(out);
                    testResult = STATUS_FAILED;
                    continue;
                }

                out.println("\n>  testSourceLineInfo["+j+"] = " + testSourceLineInfo);
                out.println(">  testSourceLine = " + testSourceLine);
                if ( ! (testSourceLine instanceof SourceDataLine) ) {
                    out.println(">  testSourceLine is not SourceDataLine");
                    continue;
                }

                SourceDataLine testedSourceLine = (SourceDataLine)testSourceLine;
                AudioFormat lineAudioFormat = testedSourceLine.getFormat();

                int bufferSizeToWrite = 1;
                if ( lineAudioFormat.getSampleSizeInBits() != AudioSystem.NOT_SPECIFIED ) {
                    bufferSizeToWrite = lineAudioFormat.getSampleSizeInBits()/8;
                    if ( lineAudioFormat.getSampleSizeInBits()%8 != 0 ) {
                        bufferSizeToWrite++;
                    }
                }
                if ( lineAudioFormat.getFrameSize() != AudioSystem.NOT_SPECIFIED ) {
                    bufferSizeToWrite = lineAudioFormat.getFrameSize();
                }
                byte[] dataToWrite = new byte[bufferSizeToWrite];
                for (int k=0; k < bufferSizeToWrite; k++) {
                    dataToWrite[k] = (byte)1;
                }
                int offsetToWrite = 0;

                out.println
                    ("\n>  check SourceDataLine.write() for not open line with correct length of data:");
                int writtenBytes = -1;
                try {
                    writtenBytes = testedSourceLine.write(dataToWrite, offsetToWrite, bufferSizeToWrite);
                    out.println(">  Bytes written: number of written bytes = " + writtenBytes);
                } catch (Throwable thrown) {
                    out.println("## SourceDataLine.write(byte[] b, int off, int len) failed:");
                    out.println("#  Unexpected Exception is thrown");
                    out.println("#  Mixer = " + testedMixer);
                    out.println("#  SourceDataLine = " + testedSourceLine);
                    thrown.printStackTrace(out);
                    testResult = STATUS_FAILED;
                }

                out.println
                    ("\n>  check SourceDataLine.write() for not open line with incorrect length of data:");
                writtenBytes = -1;
                bufferSizeToWrite--;
                try {
                    writtenBytes = testedSourceLine.write(dataToWrite, offsetToWrite, bufferSizeToWrite);
                    out.println(">  Bytes written: number of written bytes = " + writtenBytes);
                } catch (IllegalArgumentException illegalArgumentException) {
                    out.println(">  Permissible IllegalArgumentException for the present instance is thrown:");
                    illegalArgumentException.printStackTrace(out);
                } catch (Throwable thrown) {
                    out.println("## SourceDataLine.write(byte[] b, int off, int len) failed:");
                    out.println("#  Unexpected Exception is thrown");
                    out.println("#  Mixer = " + testedMixer);
                    out.println("#  SourceDataLine = " + testedSourceLine);
                    thrown.printStackTrace(out);
                    testResult = STATUS_FAILED;
                }

                out.println
                    ("\n>  open tested line:");
                bufferSizeToWrite++;
                try {
                    testedSourceLine.open(lineAudioFormat, bufferSizeToWrite);
                    out.println(">  OK - line is opened");
                } catch (LineUnavailableException lineUnavailableException) {
                    out.println(">  Line is not available due to resource restrictions:");
                    lineUnavailableException.printStackTrace(out);
                    continue;
                } catch (SecurityException securityException) {
                    out.println("> Line is not available due to security restrictions:");
                    securityException.printStackTrace(out);
                    continue;
                } catch (Throwable thrown) {
                    out.println("## SourceDataLine.open(AudioFormat format) failed:");
                    out.println("#  Unexpected Exception is thrown");
                    out.println("#  Mixer = " + testedMixer);
                    out.println("#  SourceDataLine = " + testedSourceLine);
                    thrown.printStackTrace(out);
                    testResult = STATUS_FAILED;
                    continue;
                }

                out.println
                    ("\n>  check SourceDataLine.write() for not started line with correct length of data:");
                writtenBytes = -1;
                try {
                    writtenBytes = testedSourceLine.write(dataToWrite, offsetToWrite, bufferSizeToWrite);
                    out.println(">  Bytes written: number of written bytes = " + writtenBytes);
                } catch (Throwable thrown) {
                    out.println("## SourceDataLine.write(byte[] b, int off, int len) failed:");
                    out.println("#  Unexpected Exception is thrown");
                    out.println("#  Mixer = " + testedMixer);
                    out.println("#  SourceDataLine = " + testedSourceLine);
                    thrown.printStackTrace(out);
                    testResult = STATUS_FAILED;
                }

                out.println
                    ("\n>  check SourceDataLine.write() for not started line with incorrect length of data:");
                writtenBytes = -1;
                bufferSizeToWrite--;
                try {
                    writtenBytes = testedSourceLine.write(dataToWrite, offsetToWrite, bufferSizeToWrite);
                    out.println(">  Bytes written: number of written bytes = " + writtenBytes);
                } catch (IllegalArgumentException illegalArgumentException) {
                    out.println(">  Permissible IllegalArgumentException for the present instance is thrown:");
                    illegalArgumentException.printStackTrace(out);
                } catch (Throwable thrown) {
                    out.println("## SourceDataLine.write(byte[] b, int off, int len) failed:");
                    out.println("#  Unexpected Exception is thrown");
                    out.println("#  Mixer = " + testedMixer);
                    out.println("#  SourceDataLine = " + testedSourceLine);
                    thrown.printStackTrace(out);
                    testResult = STATUS_FAILED;
                }
                testedSourceLine.close();

            }  
            testedMixer.close();

        }  

        if ( testResult == STATUS_FAILED ) {
            out.println("\n==> test FAILED!");
        } else {
            out.println("\n==> test PASSED!");
        }
        return testResult;
    }
}
