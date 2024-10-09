
package util;

import java.io.Serializable;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.Map;

public class StubRef implements Ref, Serializable {

    private final String baseTypeName;
    private Object obj;

    public StubRef(String type, Object o) {
        baseTypeName = type;
        obj = o;
    }

    @Override
    public String getBaseTypeName() throws SQLException {
        return baseTypeName;
    }

    @Override
    public Object getObject(Map<String, Class<?>> map) throws SQLException {
        return obj;
    }

    @Override
    public Object getObject() throws SQLException {
        return getObject(null);
    }

    @Override
    public void setObject(Object value) throws SQLException {
        obj = value;
    }

}
