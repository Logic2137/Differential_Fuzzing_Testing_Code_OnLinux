import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class PollMemoryLeak {

    public static void main(String[] args) throws Throwable {
        new PollMemoryLeak().main();
    }

    void main() throws Throwable {
        test(new LinkedBlockingDeque(10));
        test(new LinkedBlockingQueue(10));
        test(new LinkedTransferQueue());
        test(new ArrayBlockingQueue(10));
        test(new PriorityBlockingQueue());
        test(new SynchronousQueue());
        test(new SynchronousQueue(true));
    }

    void test(BlockingQueue q) throws Throwable {
        assertNoLeak(q, () -> timedPoll(q));
    }

    static void timedPoll(BlockingQueue q) {
        try {
            q.poll(1, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ex) {
            throw new AssertionError(ex);
        }
    }

    void assertNoLeak(Object root, Runnable r) {
        int prev = retainedObjects(root).size();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) r.run();
            int next = retainedObjects(root).size();
            if (next <= prev)
                return;
            prev = next;
        }
        throw new AssertionError(String.format("probable memory leak in %s: %s", root.getClass().getSimpleName(), root));
    }

    ConcurrentHashMap<Class<?>, Collection<Field>> classFields = new ConcurrentHashMap<>();

    Collection<Field> referenceFieldsOf(Class<?> k) {
        Collection<Field> fields = classFields.get(k);
        if (fields == null) {
            fields = new ArrayDeque<Field>();
            ArrayDeque<Field> allFields = new ArrayDeque<>();
            for (Class<?> c = k; c != null; c = c.getSuperclass()) for (Field field : c.getDeclaredFields()) if (!Modifier.isStatic(field.getModifiers()) && !field.getType().isPrimitive())
                fields.add(field);
            AccessibleObject.setAccessible(fields.toArray(new AccessibleObject[0]), true);
            classFields.put(k, fields);
        }
        return fields;
    }

    static Object get(Field field, Object x) {
        try {
            return field.get(x);
        } catch (IllegalAccessException ex) {
            throw new AssertionError(ex);
        }
    }

    Set<Object> retainedObjects(Object x) {
        ArrayDeque<Object> todo = new ArrayDeque<>() {

            public void push(Object x) {
                if (x != null)
                    super.push(x);
            }
        };
        Set<Object> uniqueObjects = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
        todo.push(x);
        while (!todo.isEmpty()) {
            Object y = todo.pop();
            if (uniqueObjects.contains(y))
                continue;
            uniqueObjects.add(y);
            Class<?> k = y.getClass();
            if (k.isArray() && !k.getComponentType().isPrimitive()) {
                for (int i = 0, len = Array.getLength(y); i < len; i++) todo.push(Array.get(y, i));
            } else {
                for (Field field : referenceFieldsOf(k)) todo.push(get(field, y));
            }
        }
        return uniqueObjects;
    }

    void printRetainedObjects(Object x) {
        for (Object y : retainedObjects(x)) System.out.printf("%s : %s%n", y.getClass().getSimpleName(), y);
    }
}
