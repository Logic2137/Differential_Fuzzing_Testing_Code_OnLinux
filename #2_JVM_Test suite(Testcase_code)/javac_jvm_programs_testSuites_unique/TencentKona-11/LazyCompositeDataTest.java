

import java.util.HashMap;
import java.util.Map;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import sun.management.LazyCompositeData;



public class LazyCompositeDataTest {
    private final static CompositeData dataV1, dataV2;

    static {
        try {
            
            

            
            CompositeType subtypeV1 = new CompositeType(
                    "Subtype1",
                    "Version 1",
                    new String[]{"item1"},
                    new String[]{"Item 1"},
                    new OpenType<?>[]{
                        SimpleType.STRING
                    }
            );

            
            CompositeType subtypeV2 = new CompositeType(
                    "Subtype2",
                    "Version 2",
                    new String[]{"item1", "item2"},
                    new String[]{"Item 1", "Item 2"},
                    new OpenType<?>[]{
                        SimpleType.STRING,
                        SimpleType.INTEGER
                    }
            );


            
            
            CompositeType typeV1 = new CompositeType(
                    "MyDataV1",
                    "Version 1",
                    new String[]{"item1", "item2"},
                    new String[]{"Item 1", "Item 2"},
                    new OpenType<?>[]{
                        SimpleType.STRING,
                        ArrayType.getArrayType(subtypeV1)
                    }
            );

            
            
            CompositeType typeV2 = new CompositeType(
                    "MyDataV2",
                    "Version 2",
                    new String[]{"item1", "item2"},
                    new String[]{"Item 1", "Item 2"},
                    new OpenType<?>[]{
                        SimpleType.STRING,
                        ArrayType.getArrayType(subtypeV2)
                    }
            );
            

            
            
            Map<String, Object> subitemsV1 = new HashMap<>();
            Map<String, Object> subitemsV2 = new HashMap<>();

            Map<String, Object> itemsV1 = new HashMap<>();
            Map<String, Object> itemsV2 = new HashMap<>();

            subitemsV1.put("item1", "item1");
            subitemsV2.put("item1", "item1");
            subitemsV2.put("item2", 42);

            itemsV1.put("item1", "item1");
            itemsV1.put("item2", new CompositeData[]{new CompositeDataSupport(subtypeV1, subitemsV1)});

            itemsV2.put("item1", "item1");
            itemsV2.put("item2", new CompositeData[]{new CompositeDataSupport(subtypeV2, subitemsV2)});

            dataV1 = new CompositeDataSupport(typeV1, itemsV1);
            dataV2 = new CompositeDataSupport(typeV2, itemsV2);
            
        } catch (OpenDataException e) {
            throw new Error(e);
        }
    }

    private static class MyDataV1 extends LazyCompositeData {
        @Override
        protected CompositeData getCompositeData() {
            return dataV1;
        }

        public boolean isTypeMached(CompositeType type) {
            return isTypeMatched(this.getCompositeType(), type);
        }
    }

    private static class MyDataV2 extends LazyCompositeData {
        @Override
        protected CompositeData getCompositeData() {
            return dataV2;
        }

    }

    public static void main(String[] args) throws Exception {
        System.out.println("Checking LazyCompositeData.isTypeMatched()");
        MyDataV1 v1 = new MyDataV1();
        MyDataV2 v2 = new MyDataV2();

        if (!v1.isTypeMached(v2.getCompositeType())) {
            System.err.println("=== FAILED");
            System.err.println("V1 should be matched by V2");
            System.err.println("\n=== V1");
            System.err.println(v1.getCompositeType());
            System.err.println("\n=== V2");
            System.err.println(v2.getCompositeType());
            throw new Error();
        }
        System.out.println("=== PASSED");
    }
}

