

package nsk.share.gc.gp;


public interface GarbageProducer<T> {
        
        public T create(long memory);

        
        public void validate(T obj);
}
