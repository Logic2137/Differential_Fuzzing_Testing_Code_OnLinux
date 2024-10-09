











import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import jdk.dynalink.DynamicLinker;
import jdk.dynalink.DynamicLinkerFactory;
import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.GuardingDynamicLinker;
import jdk.dynalink.linker.GuardingTypeConverterFactory;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;


public class TypeConverterFactoryMemoryLeakTest {
    
    
    private static final int MAX_ITERATIONS = 1000;

    private static final ReferenceQueue<MethodHandle> refQueue = new ReferenceQueue<>();
    private static final List<Reference<MethodHandle>> refs = new ArrayList<>();

    private static class TestLinker implements GuardingDynamicLinker, GuardingTypeConverterFactory {
        public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) {
            
            throw new UnsupportedOperationException();
        }

        public GuardedInvocation convertToType(Class<?> sourceType, Class<?> targetType, Supplier<MethodHandles.Lookup> lookupSupplier) {
            
            MethodHandle result = MethodHandles.empty(MethodType.methodType(targetType, sourceType));
            
            refs.add(new PhantomReference<>(result, refQueue));
            return new GuardedInvocation(result);
        }
    }

    public static void main(String[] args) {
        for (int count = 0; count < MAX_ITERATIONS; count++) {
            
            makeOne();
            System.gc();
            if (refQueue.poll() != null) {
                
                return;
            }
        }
        
        throw new AssertionError("Should have GCd a method handle by now");
    }

    private static void makeOne() {
        
        DynamicLinkerFactory f = new DynamicLinkerFactory();
        f.setFallbackLinkers();
        f.setPrioritizedLinker(new TestLinker());
        DynamicLinker linker = f.createLinker();
        
        
        
        linker.getLinkerServices().getTypeConverter(double.class, int.class);
    }
}
