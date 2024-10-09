
package util;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;

public class TestRowSetListener implements RowSetListener {

    public final static int ROWSET_CHANGED = 1;

    public final static int ROW_CHANGED = 2;

    public final static int CURSOR_MOVED = 4;

    private int flag;

    @Override
    public void rowSetChanged(RowSetEvent event) {
        flag |= ROWSET_CHANGED;
    }

    @Override
    public void rowChanged(RowSetEvent event) {
        flag |= ROW_CHANGED;
    }

    @Override
    public void cursorMoved(RowSetEvent event) {
        flag |= CURSOR_MOVED;
    }

    public void resetFlag() {
        flag = 0;
    }

    public boolean isNotified(int val) {
        return flag == val;
    }
}
