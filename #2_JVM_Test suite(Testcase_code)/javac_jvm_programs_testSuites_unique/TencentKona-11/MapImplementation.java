



import java.util.Map;


public interface MapImplementation {
    
    public Class<?> klazz();
    
    public Map emptyMap();
    public Object makeKey(int i);
    public Object makeValue(int i);
    public boolean isConcurrent();
    public boolean permitsNullKeys();
    public boolean permitsNullValues();
    public boolean supportsSetValue();
}
