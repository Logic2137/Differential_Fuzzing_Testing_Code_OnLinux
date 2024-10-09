




package jdk.test.lib.hprof.parser;

import java.io.IOException;


public interface ReadBuffer extends AutoCloseable {
    
    
    public void  get(long pos, byte[] buf) throws IOException;
    public char  getChar(long pos) throws IOException;
    public byte  getByte(long pos) throws IOException;
    public short getShort(long pos) throws IOException;
    public int   getInt(long pos) throws IOException;
    public long  getLong(long pos) throws IOException;
}
