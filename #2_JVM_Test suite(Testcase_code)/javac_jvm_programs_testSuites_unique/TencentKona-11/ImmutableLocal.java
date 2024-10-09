


public class ImmutableLocal
{
    
    public abstract static class ImmutableThreadLocal extends ThreadLocal {
        public void set(final Object value) {
            throw new RuntimeException("ImmutableThreadLocal set called");
        }

        
        protected abstract Object initialValue();
    }

    private static final ThreadLocal cache = new ImmutableThreadLocal() {
        public Object initialValue() {
            return Thread.currentThread().getName();
        }
    };

    public static void main(final String[] args) {
        System.out.println("cache.get() = " + cache.get());
    }
}
