
package util;

import java.sql.RowId;

public class StubRowId implements RowId {

    @Override
    public byte[] getBytes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
