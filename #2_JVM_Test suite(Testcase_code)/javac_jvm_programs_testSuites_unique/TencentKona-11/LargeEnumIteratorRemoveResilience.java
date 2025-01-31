





import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

public class LargeEnumIteratorRemoveResilience {
    
    private static enum LargeEnum {
        e00, e01, e02, e03, e04, e05, e06, e07,
        e08, e09, e0A, e0B, e0C, e0D, e0E, e0F,
        e10, e11, e12, e13, e14, e15, e16, e17,
        e18, e19, e1A, e1B, e1C, e1D, e1E, e1F,
        e20, e21, e22, e23, e24, e25, e26, e27,
        e28, e29, e2A, e2B, e2C, e2D, e2E, e2F,
        e30, e31, e32, e33, e34, e35, e36, e37,
        e38, e39, e3A, e3B, e3C, e3D, e3E, e3F,
        e40, e41, e42, e43, e44, e45, e46, e47,
        e48, e49, e4A, e4B, e4C, e4D, e4E, e4F,
    }

    public static void main(final String[] args) throws Exception {
        final Set<LargeEnum> set = EnumSet.noneOf(LargeEnum.class);

        set.add(LargeEnum.e2D);
        set.add(LargeEnum.e42);

        final Iterator<LargeEnum> iterator = set.iterator();

        int size = set.size();
        LargeEnum element = iterator.next();

        iterator.remove();
        checkSetAfterRemoval(set, size, element);

        size = set.size();
        element = iterator.next();

        set.remove(element);
        checkSetAfterRemoval(set, size, element);

        
        
        
        
        
        
        
        
        iterator.remove();
        checkSetAfterRemoval(set, size, element);
    }

    private static void checkSetAfterRemoval(final Set<LargeEnum> set,
            final int origSize, final LargeEnum removedElement)
            throws Exception {
        if (set.size() != (origSize - 1)) {
            throw new Exception("Test FAILED: Unexpected set size after removal; expected '" + (origSize - 1) + "' but found '" + set.size() + "'");
        }
        if (set.contains(removedElement)) {
            throw new Exception("Test FAILED: Element returned from iterator unexpectedly still in set after removal.");
        }
    }
}
