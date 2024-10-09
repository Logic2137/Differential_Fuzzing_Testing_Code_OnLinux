



import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.ImageTypeSpecifier;

public class GetObjectMinValue {

    public static void main(String argv[]) {
        test(true, true);
        test(true, false);
        test(false, true);
        test(false, false);
    }

    private static void test(boolean minInclusive, boolean maxInclusive) {
        Integer defValue = new Integer(1);
        Integer minValue = new Integer(0);
        Integer maxValue = new Integer(10);

        MyFormatImpl fmt = new MyFormatImpl("root", 1, 10);

        fmt.addObjectValue("root", defValue.getClass(), defValue,
                           minValue, maxValue, minInclusive, maxInclusive);

        try {
            Integer act_min = (Integer)fmt.getObjectMinValue("root");
            if (! act_min.equals(minValue))
                throw new RuntimeException("invalid min value: " + act_min);
        } catch (Throwable e) {
            throw new RuntimeException
                ("getObjectMinValue: unexpected exception: " + e);
        }
        try {
            Integer act_max = (Integer)fmt.getObjectMaxValue("root");
            if (! act_max.equals(maxValue))
                throw new RuntimeException("invalid max value: " + act_max);
        } catch (Throwable e) {
            throw new RuntimeException
                ("getObjectMaxValue: unexpected exception: " + e);
        }
    }

    static class MyFormatImpl extends IIOMetadataFormatImpl {

        MyFormatImpl(String root, int minChildren, int maxChildren) {
            super(root, minChildren, maxChildren);
        }

        public void addObjectValue(String elementName,
                                   Class<?> classType, Integer defaultValue,
                                   Comparable minValue, Comparable maxValue,
                                   boolean minInclusive, boolean maxInclusive) {
            super.<Integer>addObjectValue(elementName,
                                          (Class<Integer>)classType, defaultValue,
                                          (Comparable<? super Integer>) minValue, (Comparable<? super Integer>) maxValue,
                                          minInclusive, maxInclusive);
        }

        public boolean canNodeAppear(String elementName,
                                     ImageTypeSpecifier imageType) {
            return true;
        }
    }

}
