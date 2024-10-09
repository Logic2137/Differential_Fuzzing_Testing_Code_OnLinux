
package util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class BOMInputStream {
        public static InputStream createStream(String charset, InputStream input) {

                        try {
                                byte[] content = read(input).getBytes(charset);
                                byte[] head = bom.get(charset);
                                if (head == null)
                                        return null;
                                byte[] result = new byte[head.length + content.length];
                                System.arraycopy(head, 0, result, 0, head.length);
                                System.arraycopy(content, 0, result, head.length, content.length);
                                return new ByteArrayInputStream(result);
                        } catch (UnsupportedEncodingException e) {
                                return null;
                        }
        }

        private static String read(InputStream input)
        {
                try {
                        StringBuffer sb = new StringBuffer();
                        InputStreamReader r = new InputStreamReader(new BufferedInputStream(input));
                        int c = 0;
                        while ((c = r.read()) != -1)
                                sb.append((char)c);
                        return sb.toString();
                } catch (IOException e) {
                        return "";
                } finally {
                        try {
                                input.close();
                        } catch (IOException e)
                        {}
                }
        }


        private final static Map<String, byte[]> bom = new HashMap();
        private final static byte[][] bomBytes = {{(byte)0xEF, (byte)0xBB, (byte)0xBF},
                                        {(byte)0xFE, (byte)0xFF}};

        static {
                bom.put("UTF-8", bomBytes[0]);
                bom.put("UTF-16BE", bomBytes[1]);
        }

}
