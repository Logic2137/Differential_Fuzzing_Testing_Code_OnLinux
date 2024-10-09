
package util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.SQLXML;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

public class StubSQLXML implements SQLXML {

    @Override
    public void free() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputStream setBinaryStream() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Writer setCharacterStream() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getString() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setString(String value) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Source> T getSource(Class<T> sourceClass) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Result> T setResult(Class<T> resultClass) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
