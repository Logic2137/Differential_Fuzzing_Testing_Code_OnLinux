
package util;

import java.sql.Array;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

public class StubArray implements Array {

    private String typeName;

    Object[] elements;

    public StubArray(String name, Object[] values) {
        typeName = name;
        elements = Arrays.copyOf(values, values.length);
    }

    @Override
    public String getBaseTypeName() throws SQLException {
        return typeName;
    }

    @Override
    public int getBaseType() throws SQLException {
        return JDBCType.valueOf(typeName).getVendorTypeNumber();
    }

    @Override
    public Object getArray() throws SQLException {
        return Arrays.copyOf(elements, elements.length);
    }

    @Override
    public Object getArray(Map<String, Class<?>> map) throws SQLException {
        return getArray();
    }

    @Override
    public Object getArray(long index, int count) throws SQLException {
        return getArray();
    }

    @Override
    public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
        return getArray();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResultSet getResultSet(long index, int count) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void free() throws SQLException {
        elements = null;
        typeName = null;
    }
}
