



import java.io.*;
import java.security.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;


public abstract class AbstractObjectOutputStream extends ObjectOutputStream
{
    protected OutputStream out;
    

    
    public AbstractObjectOutputStream(OutputStream out) throws IOException {
        this.out = out;
    }

    public abstract void reset() throws IOException;
    protected abstract void drain() throws IOException;
    public abstract void close() throws IOException;

    

    

    
    protected void writeObjectOverride(Object obj)
        throws IOException
    {
    }

    
    public abstract void defaultWriteObject() throws IOException;

    
    

    public abstract ObjectOutputStream.PutField putFields() throws IOException;

    
    public abstract void writeFields() throws IOException;

    protected abstract boolean enableReplaceObject(boolean enable) throws SecurityException;

    
    

    public abstract void write(int data) throws IOException;
    public abstract void write(byte b[]) throws IOException;
    public abstract void write(byte b[], int off, int len) throws IOException;
    public abstract void writeBoolean(boolean data) throws IOException;
    public abstract void writeByte(int data) throws IOException;
    public abstract void writeShort(int data)  throws IOException;
    public abstract void writeChar(int data)  throws IOException;
    public abstract void writeInt(int data)  throws IOException;
    public abstract void writeLong(long data)  throws IOException;
    public abstract void writeFloat(float data) throws IOException;
    public abstract void writeDouble(double data) throws IOException;
    public abstract void writeBytes(String data) throws IOException;
    public abstract void writeChars(String data) throws IOException;
    public abstract void writeUTF(String data) throws IOException;
};
