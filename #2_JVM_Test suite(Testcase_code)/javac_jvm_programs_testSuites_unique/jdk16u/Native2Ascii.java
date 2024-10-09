

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FilterReader;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.charset.StandardCharsets.*;


public class Native2Ascii {
    final Charset cs;
    final CharsetEncoder encoder;
    public Native2Ascii(Charset cs) {
        this.cs = cs;
        this.encoder = cs.newEncoder();
    }

    
    public void asciiToNative(Path infile, Path outfile) throws IOException {
        try (BufferedReader in = Files.newBufferedReader(infile, US_ASCII);
             BufferedReader reader = new BufferedReader(new A2NFilter(in));
             BufferedWriter writer = Files.newBufferedWriter(outfile, cs)) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line.toCharArray());
                writer.newLine();
            }
        }
    }

    
    public void nativeToAscii(Path infile, Path outfile) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(infile, cs);
             BufferedWriter out = Files.newBufferedWriter(outfile, US_ASCII);
             BufferedWriter writer = new BufferedWriter(new N2AFilter(out))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line.toCharArray());
                writer.newLine();
            }
        }
    }

    
    class N2AFilter extends FilterWriter {
        public N2AFilter(Writer out) { super(out); }
        public void write(char b) throws IOException {
            char[] buf = new char[1];
            buf[0] = b;
            write(buf, 0, 1);
        }

        public void write(char[] buf, int off, int len) throws IOException {
            for (int i = 0; i < len; i++) {
                if ((buf[i] > '\u007f')) {
                    
                    out.write('\\');
                    out.write('u');
                    String hex = Integer.toHexString(buf[i]);
                    StringBuilder hex4 = new StringBuilder(hex);
                    hex4.reverse();
                    int length = 4 - hex4.length();
                    for (int j = 0; j < length; j++) {
                        hex4.append('0');
                    }
                    for (int j = 0; j < 4; j++) {
                        out.write(hex4.charAt(3 - j));
                    }
                } else
                    out.write(buf[i]);
            }
        }
    }

    
    class A2NFilter extends FilterReader {
        
        
        private char[] trailChars = null;

        public A2NFilter(Reader in) {
            super(in);
        }

        public int read(char[] buf, int off, int len) throws IOException {
            int numChars = 0;        
            int retChars = 0;        

            char[] cBuf = new char[len];
            int cOffset = 0;         
            boolean eof = false;

            
            if (trailChars != null) {
                for (int i = 0; i < trailChars.length; i++)
                    cBuf[i] = trailChars[i];
                numChars = trailChars.length;
                trailChars = null;
            }

            int n = in.read(cBuf, numChars, len - numChars);
            if (n < 0) {
                eof = true;
                if (numChars == 0)
                    return -1;              
            } else {
                numChars += n;
            }

            for (int i = 0; i < numChars; ) {
                char c = cBuf[i++];

                if (c != '\\' || (eof && numChars <= 5)) {
                    
                    
                    
                    
                    
                    
                    
                    buf[retChars++] = c;
                    continue;
                }

                int remaining = numChars - i;
                if (remaining < 5) {
                    
                    
                    trailChars = new char[1 + remaining];
                    trailChars[0] = c;
                    for (int j = 0; j < remaining; j++)
                        trailChars[1 + j] = cBuf[i + j];
                    break;
                }
                

                c = cBuf[i++];
                if (c != 'u') {
                    
                    buf[retChars++] = '\\';
                    buf[retChars++] = c;
                    continue;
                }

                
                char rc = 0;
                boolean isUE = true;
                try {
                    rc = (char) Integer.parseInt(new String(cBuf, i, 4), 16);
                } catch (NumberFormatException x) {
                    isUE = false;
                }
                if (isUE && encoder.canEncode(rc)) {
                    
                    buf[retChars++] = rc;
                    i += 4; 
                } else {
                    
                    buf[retChars++] = '\\';
                    buf[retChars++] = 'u';
                    continue;
                }

            }

            return retChars;
        }

        public int read() throws IOException {
            char[] buf = new char[1];

            if (read(buf, 0, 1) == -1)
                return -1;
            else
                return (int) buf[0];
        }
    }
}
