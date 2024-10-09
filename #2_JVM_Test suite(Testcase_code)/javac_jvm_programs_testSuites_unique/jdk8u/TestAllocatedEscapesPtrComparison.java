import java.util.ArrayList;
import java.util.List;

public class TestAllocatedEscapesPtrComparison {

    static TestAllocatedEscapesPtrComparison dummy;

    class Marker {
    }

    List<Marker> markerList = new ArrayList<>();

    Marker getMarker() {
        final Marker result = new Marker();
        markerList.add(result);
        return result;
    }

    void visit(int depth) {
        getMarker();
        if (depth % 10 == 2) {
            visitAndPop(depth + 1);
        } else if (depth < 15) {
            visit(depth + 1);
        }
    }

    void visitAndPop(int depth) {
        dummy = new TestAllocatedEscapesPtrComparison();
        Marker marker = getMarker();
        visit(depth + 1);
        boolean found = false;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = markerList.size() - 1; i >= 0; i--) {
            Marker removed = markerList.remove(i);
            if (removed == marker) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new RuntimeException("test fails");
        }
    }

    public static void main(String[] args) {
        TestAllocatedEscapesPtrComparison tc = new TestAllocatedEscapesPtrComparison();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 20000; i++) {
            tc.visit(0);
        }
    }
}
