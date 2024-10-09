

package jdk.testlibrary;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;


public final class OptimalCapacity {

    private OptimalCapacity() {}

    
    public static void ofArrayList(Class<?> clazz, String fieldName,
            int initialCapacity)
    {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object obj = field.get(null);
            if (!ArrayList.class.equals(obj.getClass())) {
                throw new RuntimeException("'" + field +
                    "' expected to be of type ArrayList");
            }
            ArrayList<?> list = (ArrayList<?>)obj;

            
            if (list.size() != initialCapacity) {
                throw new RuntimeException("Size of '" + field +
                    "' is " + list.size() +
                    ", but expected to be " + initialCapacity);
            }
            if (internalArraySize(list) != initialCapacity) {
                throw new RuntimeException("Capacity of '" + field +
                    "' is " + internalArraySize(list) +
                    ", but expected to be " + initialCapacity);
            }
        } catch (ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    
    public static void ofHashMap(Class<?> clazz, String fieldName,
            int initialCapacity)
    {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object obj = field.get(null);
            if (!HashMap.class.equals(obj.getClass())) {
                throw new RuntimeException(field +
                    " expected to be of type HashMap");
            }
            HashMap<?,?> map = (HashMap<?,?>)obj;

            
            HashMap<Object, Object> tmp = new HashMap<>(map);
            if (internalArraySize(map) != internalArraySize(tmp)) {
                throw new RuntimeException("Final capacity of '" + field +
                    "' is " + internalArraySize(map) +
                    ", which exceeds necessary minimum " + internalArraySize(tmp));
            }

            
            tmp = new HashMap<>(initialCapacity);
            tmp.put(new Object(), new Object()); 
            if (internalArraySize(map) != internalArraySize(tmp)) {
                throw new RuntimeException("Requested capacity of '" + field +
                    "' was " + initialCapacity +
                    ", which resulted in final capacity " + internalArraySize(tmp) +
                    ", which differs from necessary minimum " + internalArraySize(map));
            }

        } catch (ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    
    public static void ofIdentityHashMap(Class<?> clazz, String fieldName,
            int expectedMaxSize)
    {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object obj = field.get(null);
            if (!IdentityHashMap.class.equals(obj.getClass())) {
                throw new RuntimeException("'" + field +
                    "' expected to be of type IdentityHashMap");
            }
            IdentityHashMap<?,?> map = (IdentityHashMap<?,?>)obj;

            
            if (map.size() != expectedMaxSize) {
                throw new RuntimeException("Size of '" + field +
                    "' is " + map.size() +
                    ", which differs from expected " + expectedMaxSize);
            }

            
            IdentityHashMap<Object, Object> tmp = new IdentityHashMap<>(map);
            if (internalArraySize(map) != internalArraySize(tmp)) {
                throw new RuntimeException("Final capacity of '" + field +
                    "' is " + internalArraySize(map) +
                    ", which exceeds necessary minimum " + internalArraySize(tmp));
            }

            
            tmp = new IdentityHashMap<>(expectedMaxSize);
            tmp.put(new Object(), new Object()); 
            if (internalArraySize(map) != internalArraySize(tmp)) {
                throw new RuntimeException("Requested number of elements in '" + field +
                    "' was " + expectedMaxSize +
                    ", which resulted in final capacity " + internalArraySize(tmp) +
                    ", which differs from necessary minimum " + internalArraySize(map));
            }
        } catch (ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    
    private static int internalArraySize(Object container)
            throws ReflectiveOperationException {
        Field field;
        if (ArrayList.class.equals(container.getClass())) {
            field = ArrayList.class.getDeclaredField("elementData");
        } else if (HashMap.class.equals(container.getClass())) {
            field = HashMap.class.getDeclaredField("table");
        } else if (IdentityHashMap.class.equals(container.getClass())) {
            field = IdentityHashMap.class.getDeclaredField("table");
        } else {
            throw new RuntimeException("Unexpected class " +
                    container.getClass());
        }
        field.setAccessible(true);
        return ((Object[])field.get(container)).length;
    }
}
