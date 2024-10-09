



import java.lang.ref.*;

public class ReferenceClone {
    private static final ReferenceQueue<Object> QUEUE = new ReferenceQueue<>();
    public static void main(String... args) {
        ReferenceClone refClone = new ReferenceClone();
        refClone.test();
    }

    public void test() {
        
        Object o = new Object();
        assertCloneNotSupported(new SoftRef(o));
        assertCloneNotSupported(new WeakRef(o));
        assertCloneNotSupported(new PhantomRef(o));

        
        CloneableReference ref = new CloneableReference(o);
        try {
            ref.clone();
        } catch (CloneNotSupportedException e) {}
    }

    private void assertCloneNotSupported(CloneableRef ref) {
        try {
            ref.clone();
            throw new RuntimeException("Reference::clone should throw CloneNotSupportedException");
        } catch (CloneNotSupportedException e) {}
    }

    
    interface CloneableRef extends Cloneable {
        public Object clone() throws CloneNotSupportedException;
    }

    class SoftRef extends SoftReference<Object> implements CloneableRef {
        public SoftRef(Object referent) {
            super(referent, QUEUE);
        }
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    class WeakRef extends WeakReference<Object> implements CloneableRef {
        public WeakRef(Object referent) {
            super(referent, QUEUE);
        }
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    class PhantomRef extends PhantomReference<Object> implements CloneableRef {
        public PhantomRef(Object referent) {
            super(referent, QUEUE);
        }

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    
    class CloneableReference extends WeakReference<Object> implements Cloneable {
        public CloneableReference(Object referent) {
            super(referent, QUEUE);
        }

        public Object clone() throws CloneNotSupportedException {
            return new CloneableReference(this.get());
        }
    }

}
