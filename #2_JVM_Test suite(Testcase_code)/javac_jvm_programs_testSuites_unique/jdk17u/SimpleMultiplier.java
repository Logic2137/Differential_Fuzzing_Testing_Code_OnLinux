
package sampleapi.util;

import java.util.StringTokenizer;
import java.util.ArrayList;

public class SimpleMultiplier {

    ArrayList<ArrayList<String>> valueSpace = new ArrayList<>();

    int size = 0;

    int index = 0;

    public void addAxis(String values) {
        ArrayList<String> valueAxis = new ArrayList<>();
        StringTokenizer valuesTokens = new StringTokenizer(values, "|");
        while (valuesTokens.hasMoreTokens()) valueAxis.add(valuesTokens.nextToken());
        valueSpace.add(valueAxis);
    }

    public void initIterator() {
        size = 1;
        if (!valueSpace.isEmpty()) {
            for (int i = 0; i < valueSpace.size(); i++) size *= valueSpace.get(i).size();
        }
        index = 0;
    }

    public boolean hasNext() {
        return index < size;
    }

    public ArrayList<String> getNext() {
        ArrayList<String> next = new ArrayList<>();
        int positionIndex = index;
        for (int i = valueSpace.size() - 1; i >= 0; i--) {
            ArrayList<String> valueAxis = valueSpace.get(i);
            int axisSize = valueAxis.size();
            next.add(valueAxis.get(positionIndex % axisSize));
            positionIndex /= axisSize;
        }
        index += 1;
        return next;
    }
}
