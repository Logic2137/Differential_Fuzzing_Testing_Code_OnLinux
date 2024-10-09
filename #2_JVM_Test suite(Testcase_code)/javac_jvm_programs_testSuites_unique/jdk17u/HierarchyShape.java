
package selectionresolution;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class HierarchyShape {

    public static final int OBJECT_CLASS = -1;

    protected int maxId;

    private final HashSet<Integer> classes;

    private final HashSet<Integer> interfaces;

    private final HashMap<Integer, HashSet<Integer>> extensions;

    public HierarchyShape() {
        this(0, new HashSet<>(), new HashSet<>(), new HashMap<>());
    }

    private HierarchyShape(final int maxId, final HashSet<Integer> classes, final HashSet<Integer> interfaces, final HashMap<Integer, HashSet<Integer>> extensions) {
        this.maxId = maxId;
        this.classes = classes;
        this.interfaces = interfaces;
        this.extensions = extensions;
    }

    public HierarchyShape copy() {
        final HashMap<Integer, HashSet<Integer>> newextensions = new HashMap<>();
        for (final Map.Entry<Integer, HashSet<Integer>> entry : extensions.entrySet()) {
            newextensions.put(entry.getKey(), (HashSet<Integer>) entry.getValue().clone());
        }
        return new HierarchyShape(maxId, (HashSet<Integer>) classes.clone(), (HashSet<Integer>) interfaces.clone(), newextensions);
    }

    public int addClass() {
        final int id = maxId++;
        classes.add(id);
        return id;
    }

    public int addInterface() {
        final int id = maxId++;
        interfaces.add(id);
        return id;
    }

    public void addInherit(final int sub, final int sup) {
        HashSet<Integer> ext = extensions.get(sub);
        if (ext == null) {
            ext = new HashSet<>();
            extensions.put(sub, ext);
        }
        ext.add(sup);
    }

    @Override
    public String toString() {
        String out = "";
        for (int i = maxId - 1; i >= 0; i--) {
            out += i + ": ";
            for (int j = 0; j < maxId; j++) {
                out += "[" + (inherits(i, j) ? "1" : "0") + "]";
            }
            out += "\n";
        }
        return out;
    }

    public boolean inherits(final int sub, final int sup) {
        final Set<Integer> ext = extensions.get(sub);
        if (ext != null) {
            return ext.contains(sup);
        } else {
            return false;
        }
    }

    public boolean isClass(final int id) {
        if (id == OBJECT_CLASS) {
            return true;
        }
        return classes.contains(id);
    }

    public boolean isInterface(final int id) {
        if (id == OBJECT_CLASS) {
            return false;
        }
        return interfaces.contains(id);
    }

    public Collection<Integer> classes() {
        return classes;
    }

    public Collection<Integer> interfaces() {
        return interfaces;
    }

    public Collection<Integer> types() {
        final Set<Integer> combined = new HashSet(classes);
        combined.addAll(interfaces);
        return combined;
    }

    public int numClasses() {
        return classes.size();
    }

    public int numInterfaces() {
        return interfaces.size();
    }

    public int numTypes() {
        return numClasses() + numInterfaces();
    }
}
