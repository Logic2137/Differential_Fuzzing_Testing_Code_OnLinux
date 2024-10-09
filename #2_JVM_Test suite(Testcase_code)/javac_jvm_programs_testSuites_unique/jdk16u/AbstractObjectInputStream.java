



import java.io.*;
import java.util.Vector;
import java.util.Stack;
import java.util.Hashtable;
import java.lang.Math;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


public abstract class AbstractObjectInputStream extends ObjectInputStream
{
    protected InputStream in;
    
    public AbstractObjectInputStream(InputStream in)
        throws IOException, StreamCorruptedException
        {
            this.in = in;
        }

    public abstract void close() throws IOException;

    
    

    
    protected Object readObjectOverride()
        throws OptionalDataException, ClassNotFoundException, IOException {
            return null;
    }

    
    public abstract void defaultReadObject()
        throws IOException, ClassNotFoundException, NotActiveException;

    
    protected final native Object
        allocateNewObject(Class<?> ofClass, Class<?> ctorClass)
        throws InstantiationException, IllegalAccessException;

    
    protected final native Object
        allocateNewArray(Class<?> componentClass, int length)
        throws InstantiationException, IllegalAccessException;

    
    public abstract ObjectInputStream.GetField readFields()
        throws IOException, ClassNotFoundException, NotActiveException;

    protected abstract boolean enableResolveObject(boolean enable) throws SecurityException;

    public abstract void registerValidation(ObjectInputValidation obj,
                                            int prio)
        throws NotActiveException, InvalidObjectException;


    

    

    public abstract int read() throws IOException;
    public abstract int read(byte[] data, int offset, int length)
        throws IOException;
    public abstract boolean readBoolean() throws IOException;
    public abstract byte readByte() throws IOException;
    public abstract int readUnsignedByte()  throws IOException;
    public abstract short readShort()  throws IOException;
    public abstract int readUnsignedShort() throws IOException;
    public abstract char readChar()  throws IOException;
    public abstract int readInt()  throws IOException;
    public abstract long readLong()  throws IOException;
    public abstract float readFloat() throws IOException;
    public abstract double readDouble() throws IOException;
    public abstract void readFully(byte[] data) throws IOException;
    public abstract void readFully(byte[] data, int offset, int size) throws IOException;
    public abstract String readUTF() throws IOException;
    public abstract int available() throws IOException;
    public abstract int skipBytes(int len) throws IOException;

    
    @SuppressWarnings("deprecation")
    public abstract String readLine() throws IOException;
};
