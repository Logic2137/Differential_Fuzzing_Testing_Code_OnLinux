

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;

final class BeanValidator {
    private final Map<Object, Object> cache = new IdentityHashMap<Object, Object>();

    public void validate(Object object1, Object object2) {
        
        if (object1 == object2) {
            return;
        }
        
        if ((object1 == null) || (object2 == null)) {
            throw new IllegalStateException("could not compare object with null");
        }
        
        if (isCyclic(object1, object2)) {
            return;
        }
        
        if (isCyclic(object2, object1)) {
            return;
        }
        Class type = object1.getClass();
        if (!type.equals(object2.getClass())) {
            
            if ((object1 instanceof Map.Entry) && (object2 instanceof Map.Entry)) {
                log("!!! special case", "Map.Entry");
                Map.Entry entry1 = (Map.Entry) object1;
                Map.Entry entry2 = (Map.Entry) object2;
                validate(entry1.getKey(), entry2.getKey());
                validate(entry1.getValue(), entry2.getValue());
                return;
            }
            throw new IllegalStateException("could not compare objects with different types");
        }
        
        if (type.isArray()) {
            int length = Array.getLength(object1);
            if (length != Array.getLength(object2)) {
                throw new IllegalStateException("could not compare arrays with different lengths");
            }
            try {
                this.cache.put(object1, object2);
                for (int i = 0; i < length; i++) {
                    log("validate array element", Integer.valueOf(i));
                    validate(Array.get(object1, i), Array.get(object2, i));
                }
            } finally {
                this.cache.remove(object1);
            }
            return;
        }
        
        boolean ignore = Collection.class.isAssignableFrom(type)
                || Map.Entry.class.isAssignableFrom(type)
                || Map.class.isAssignableFrom(type);
        
        
        
        if (!ignore && isDefined(type, "equals", Object.class)) {
            if (object1.equals(object2)) {
                return;
            }
            throw new IllegalStateException("the first object is not equal to the second one");
        }
        
        
        
        if (Comparable.class.isAssignableFrom(type) && isDefined(type, "compareTo", Object.class)) {
            Comparable cmp = (Comparable) object1;
            if (0 == cmp.compareTo(object2)) {
                return;
            }
            throw new IllegalStateException("the first comparable object is not equal to the second one");
        }
        try {
            this.cache.put(object1, object2);
            
            for (Field field : getFields(type)) {
                int mod = field.getModifiers();
                if (!Modifier.isStatic(mod)) {
                    log("validate field", field.getName());
                    validate(object1, object2, field);
                }
            }
            
            for (PropertyDescriptor pd : getDescriptors(type)) {
                Method method = pd.getReadMethod();
                if (method != null) {
                    log("validate property", pd.getName());
                    validate(object1, object2, method);
                }
            }
            
            if (SortedMap.class.isAssignableFrom(type)) {
                validate((Map) object1, (Map) object2, true);
            } else if (Map.class.isAssignableFrom(type)) {
                validate((Map) object1, (Map) object2, false);
            }
            
            if (SortedSet.class.isAssignableFrom(type)) {
                validate((Collection) object1, (Collection) object2, true);
            } else if (List.class.isAssignableFrom(type)) {
                validate((Collection) object1, (Collection) object2, true);
            } else if (Queue.class.isAssignableFrom(type)) {
                validate((Collection) object1, (Collection) object2, true);
            } else if (Collection.class.isAssignableFrom(type)) {
                validate((Collection) object1, (Collection) object2, false);
            }
        } finally {
            this.cache.remove(object1);
        }
    }

    private void validate(Object object1, Object object2, Field field) {
        try {
            object1 = field.get(object1);
            object2 = field.get(object2);

            validate(object1, object2);
        }
        catch (IllegalAccessException exception) {
            log(exception);
        }
    }

    private void validate(Object object1, Object object2, Method method) {
        try {
            object1 = method.invoke(object1);
            object2 = method.invoke(object2);

            validate(object1, object2);
        }
        catch (IllegalAccessException exception) {
            log(exception);
        }
        catch (InvocationTargetException exception) {
            log(exception.getCause());
        }
    }

    private void validate(Collection c1, Collection c2, boolean sorted) {
        if (c1.size() != c2.size()) {
            throw new IllegalStateException("could not compare collections with different sizes");
        }
        if (sorted) {
            Iterator first = c1.iterator();
            Iterator second = c2.iterator();
            for (int i = 0; first.hasNext() && second.hasNext(); i++) {
                log("validate collection element", Integer.valueOf(i));
                validate(first.next(), second.next());
            }
            if (first.hasNext() || second.hasNext()) {
                throw new IllegalStateException("one collection contains more elements than another one");
            }
        } else {
            List list = new ArrayList(c2);
            Iterator first = c1.iterator();
            for (int i = 0; first.hasNext(); i++) {
                Object value = first.next();
                log("validate collection element", Integer.valueOf(i));
                Iterator second = list.iterator();
                for (int j = 0; second.hasNext(); j++) {
                    log("validate collection element against", Integer.valueOf(j));
                    try {
                        validate(value, second.next());
                        second.remove();
                        break;
                    } catch (IllegalStateException exception) {
                        if (!second.hasNext()) {
                            throw new IllegalStateException("one collection does not contain some elements from another one", exception);
                        }
                    }
                }
            }
        }
    }

    private void validate(Map map1, Map map2, boolean sorted) {
        validate(map1.entrySet(), map2.entrySet(), sorted);
    }

    private boolean isCyclic(Object object1, Object object2) {
        Object object = this.cache.get(object1);
        if (object == null) {
            return false;
        }
        if (object == object2) {
            return true;
        }
        throw new IllegalStateException("could not resolve cyclic reference");
    }

    private boolean isDefined(Class type, String name, Class... params) {
        try {
            return type.equals(type.getMethod(name, params).getDeclaringClass());
        }
        catch (NoSuchMethodException exception) {
            log(exception);
        }
        catch (SecurityException exception) {
            log(exception);
        }
        return false;
    }

    private static final Field[] FIELDS = {};

    private Field[] getFields(Class type) {
        try {
            return type.getFields();
        }
        catch (SecurityException exception) {
            log(exception);
        }
        return FIELDS;
    }

    private static final PropertyDescriptor[] DESCRIPTORS = {};

    private PropertyDescriptor[] getDescriptors(Class type) {
        try {
            return Introspector.getBeanInfo(type, Object.class).getPropertyDescriptors();
        }
        catch (IntrospectionException exception) {
            log(exception);
        }
        return DESCRIPTORS;
    }

    private final StringBuilder sb = new StringBuilder(1024);

    private void log(String message, Object value) {
        this.sb.setLength(0);
        int size = this.cache.size();
        while (0 < size--) {
            this.sb.append("  ");
        }
        this.sb.append(" - ");
        this.sb.append(message);
        if (value != null) {
            this.sb.append(": ");
            this.sb.append(value);
        }
        System.out.println(this.sb.toString());
    }

    private void log(Throwable throwable) {
        this.sb.setLength(0);
        int size = this.cache.size();
        while (0 < size--) {
            this.sb.append("  ");
        }
        this.sb.append(" ? ");
        this.sb.append(throwable);
        System.out.println(this.sb.toString());
    }
}
