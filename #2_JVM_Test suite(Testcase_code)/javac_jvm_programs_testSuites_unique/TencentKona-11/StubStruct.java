
package util;

import java.sql.SQLException;
import java.sql.Struct;
import java.util.Arrays;
import java.util.Map;

public class StubStruct implements Struct {

    private final String type;
    private final Object[] attribs;

    public StubStruct(String type, Object[] o) {
        this.type = type;
        this.attribs = Arrays.copyOf(o, o.length);
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return type;
    }

    @Override
    public Object[] getAttributes() throws SQLException {
        return attribs;
    }

    @Override
    public Object[] getAttributes(Map<String, Class<?>> map) throws SQLException {
        return attribs;
    }

}
