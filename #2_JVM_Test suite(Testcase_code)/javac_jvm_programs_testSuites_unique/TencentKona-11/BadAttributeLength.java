




import java.io.*;

public class BadAttributeLength {

    public static String source = "public class Test {\n" +
                                  "    public static void main(String[] args) {}\n" +
                                  "}";

    public static void main(String[] args) throws Exception {
        final File sourceFile = new File("Test.java");
        if (sourceFile.exists()) {
            if (!sourceFile.delete()) {
                throw new IOException("Can't override the Test.java file. " +
                        "Check permissions.");
            }
        }
        try (FileWriter fw = new FileWriter(sourceFile)) {
            fw.write(source);
        }

        final String[] javacOpts = {"Test.java"};

        if (com.sun.tools.javac.Main.compile(javacOpts) != 0) {
            throw new Exception("Can't compile embedded test.");
        }

        RandomAccessFile raf = new RandomAccessFile("Test.class", "rw");
        long attPos = getFirstAttributePos(raf);
        if (attPos < 0) {
            throw new Exception("The class file contains no attributes at all.");
        }
        raf.seek(attPos + 2); 
        raf.writeInt(Integer.MAX_VALUE - 1);
        raf.close();

        String[] opts = { "-v", "Test.class" };
        StringWriter sw = new StringWriter();
        PrintWriter pout = new PrintWriter(sw);

        com.sun.tools.javap.Main.run(opts, pout);
        pout.flush();

        if (sw.getBuffer().indexOf("OutOfMemoryError") != -1) {
            throw new Exception("javap exited with OutOfMemoryError " +
                    "instead of giving the proper error message.");
        }
    }

    private static long getFirstAttributePos(RandomAccessFile cfile) throws Exception {
        cfile.seek(0);
        int v1, v2;
        v1 = cfile.readInt();
        

        v1 = cfile.readUnsignedShort();
        v2 = cfile.readUnsignedShort();
        

        v1 = cfile.readUnsignedShort();
        
        
        for (; v1 > 1; v1--) {
            
            byte tag = cfile.readByte();
            switch (tag) {
                case 7  : 
                case 8  : 
                    
                    cfile.skipBytes(2);
                    break;
                case 3  : 
                case 4  : 
                case 9  : 
                case 10 : 
                case 11 : 
                case 12 : 
                    
                    cfile.skipBytes(4);
                    break;
                case 5  : 
                case 6  : 
                    
                    cfile.skipBytes(8);
                    break;
                case 1  : 
                    v2 = cfile.readUnsignedShort(); 
                    cfile.skipBytes(v2); 
                    break;
                default :
                    throw new Exception("Unexpected tag in CPool: [" + tag + "] at "
                            + Long.toHexString(cfile.getFilePointer()));
            }
        }
        

        cfile.skipBytes(6); 
        v1 = cfile.readUnsignedShort(); 
        
        cfile.skipBytes(3 * v1); 
        v1 = cfile.readUnsignedShort(); 
        
        
        for (; v1 > 0; v1--) {
            
            cfile.skipBytes(6); 
            v2 = cfile.readUnsignedShort(); 
            if (v2 > 0) {
                
                
                return cfile.getFilePointer();
            }
        }
        
        v1 = cfile.readUnsignedShort(); 
        
        
        for (; v1 > 0; v1--) {
            
            cfile.skipBytes(6); 
            v2 = cfile.readUnsignedShort(); 
            if (v2 > 0) {
                
                
                
                return cfile.getFilePointer();
            }
        }
        
        
        v1 = cfile.readUnsignedShort(); 
        if (v1 > 0) {
            
            return cfile.getFilePointer();
        }
        
        return -1L;
    }
}
