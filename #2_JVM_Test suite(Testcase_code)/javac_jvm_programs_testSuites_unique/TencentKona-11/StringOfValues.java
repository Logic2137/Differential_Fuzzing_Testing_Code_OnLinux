

package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


class StringOfValues {

    private List<String> values;

    StringOfValues(String s) {
        this.values = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            values.add(st.nextToken());
        }
    }

    int getIndex(String val) {
        for (int ndx = 0; ndx < values.size(); ++ndx) {
            if (values.get(ndx).equals(val)) {
                return ndx;
            }
        }
        return -1;
    }

    String getValue(int ndx) {
        return values.get(ndx);
    }

}
