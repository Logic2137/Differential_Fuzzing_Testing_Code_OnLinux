





 

import java.lang.management.*;
import java.lang.reflect.*;
import java.util.*;

import sun.misc.*;

public class TestStringDedupStress {
    private static Field valueField;
    private static Unsafe unsafe;

    private static final int TARGET_STRINGS = Integer.getInteger("targetStrings", 2_500_000);
    private static final long MAX_REWRITE_GC_CYCLES = 6;
    private static final long MAX_REWRITE_TIME = 30*1000; 

    private static final int UNIQUE_STRINGS = 20;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            valueField = String.class.getDeclaredField("value");
            valueField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getValue(String string) {
        try {
            return valueField.get(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class StringAndId {
        private String str;
        private int id;

        public StringAndId(String str, int id) {
            this.str = str;
            this.id = id;
        }

        public String str() {
            return str;
        }

        public int id() {
            return id;
        }
    }

    
    private static void generateStrings(ArrayList<StringAndId> strs, int uniqueStrings) {
        Random rn = new Random();
        for (int u = 0; u < uniqueStrings; u++) {
            int n = rn.nextInt(uniqueStrings);
            strs.add(new StringAndId("Unique String " + n, n));
        }
    }

    private static int verifyDedupString(ArrayList<StringAndId> strs) {
        Map<Object, StringAndId> seen = new HashMap<>(TARGET_STRINGS*2);
        int total = 0;
        int dedup = 0;

        for (StringAndId item : strs) {
            total++;
            StringAndId existingItem = seen.get(getValue(item.str()));
            if (existingItem == null) {
                seen.put(getValue(item.str()), item);
            } else {
                if (item.id() != existingItem.id() ||
                        !item.str().equals(existingItem.str())) {
                    System.out.println("StringDedup error:");
                    System.out.println("id: " + item.id() + " != " + existingItem.id());
                    System.out.println("or String: " + item.str() + " != " + existingItem.str());
                    throw new RuntimeException("StringDedup Test failed");
                } else {
                    dedup++;
                }
            }
        }
        System.out.println("Dedup: " + dedup + "/" + total + " unique: " + (total - dedup));
        return (total - dedup);
    }

    static volatile ArrayList<StringAndId> astrs = new ArrayList<>();
    static GarbageCollectorMXBean gcCycleMBean;

    public static void main(String[] args) {
        Random rn = new Random();

        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            if ("Shenandoah Cycles".equals(bean.getName())) {
                gcCycleMBean = bean;
                break;
            }
        }

        if (gcCycleMBean == null) {
            throw new RuntimeException("Can not find Shenandoah GC cycle mbean");
        }

        
        int genIters = TARGET_STRINGS / UNIQUE_STRINGS;
        for (int index = 0; index < genIters; index++) {
            generateStrings(astrs, UNIQUE_STRINGS);
        }

        long cycleBeforeRewrite = gcCycleMBean.getCollectionCount();
        long timeBeforeRewrite = System.currentTimeMillis();

        long loop = 1;
        while (true) {
            int arrSize = astrs.size();
            int index = rn.nextInt(arrSize);
            StringAndId item = astrs.get(index);
            int n = rn.nextInt(UNIQUE_STRINGS);
            item.str = "Unique String " + n;
            item.id = n;

            if (loop++ % 1000 == 0) {
                
                if (gcCycleMBean.getCollectionCount() - cycleBeforeRewrite >= MAX_REWRITE_GC_CYCLES) {
                    break;
                }

                
                if (System.currentTimeMillis() - timeBeforeRewrite >= MAX_REWRITE_TIME) {
                    break;
                }
            }
        }
        verifyDedupString(astrs);
    }
}
