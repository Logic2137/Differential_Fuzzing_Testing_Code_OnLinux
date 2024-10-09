


import java.io.*;

public class ReadLine {

    public static void main(String[] args) throws IOException {
        
        
        BufferedReader reader;
        String[][] strings = {
            {"CR/LF\r\n", "CR/LF"},
            {"LF-Only\n", "LF-Only"},
            {"CR-Only\r", "CR-Only"},
            {"CR/LF line\r\nMore data", "More data"}
        };

        
        
        
        for (int i = 0; i < 3; i++) {
            reader = new BufferedReader(new
                    BoundedReader(strings[i][0]), strings[i][0].length());
            if (!reader.readLine().equals(strings[i][1]))
                throw new RuntimeException("Read incorrect text");
        }


        
        
        markResetTest("Lot of textual data\rMore textual data\n",
                "More textual data");

        
        markResetTest("Lot of textual data\r\nMore textual data\n",
                "More textual data");

        
        markResetTest("Lot of textual data\nMore textual data\n",
                "More textual data");

        
        
        

        
        reader = new BufferedReader(new
                BoundedReader(strings[3][0]), strings[3][0].length());
        reader.readLine();
        if (reader.read() != 'M')
            throw new RuntimeException("Read() failed");


        
        

        
        reader = new BufferedReader(new
                BoundedReader(strings[3][0]), strings[3][0].length());
        reader.readLine();

        char[] buf = new char[9];
        reader.read(buf, 0, 9);
        String newStr = new String(buf);
        if (!newStr.equals(strings[3][1]))
            throw new RuntimeException("Read(char[],int,int) failed");
    }

    static void markResetTest(String inputStr, String resetStr)
        throws IOException {
        BufferedReader reader = new BufferedReader(new
                BoundedReader(inputStr), inputStr.length());
        System.out.println("> " + reader.readLine());
        reader.mark(30);
        System.out.println("......Marking stream .....");
        String str = reader.readLine();
        System.out.println("> " + str);
        reader.reset();
        String newStr = reader.readLine();
        System.out.println("reset> " + newStr);

        
        if (!newStr.equals(resetStr))
            throw new RuntimeException("Mark/Reset failed");
    }


    private static class BoundedReader extends Reader{

        private char[] content;
        private int limit;
        private int pos = 0;

        public BoundedReader(String content) {
            this.limit = content.length();
            this.content = new char[limit];
            content.getChars(0, limit, this.content, 0);
        }

        public int read() throws IOException {
            if (pos >= limit)
                throw new RuntimeException("Read past limit");
            return content[pos++];
        }

        public int read(char[] buf, int offset, int length)
            throws IOException
        {
            int oldPos = pos;
            for (int i = offset; i < length; i++) {
                buf[i] = (char)read();
            }
            return (pos - oldPos);
        }

        public void close() {}
    }

}
