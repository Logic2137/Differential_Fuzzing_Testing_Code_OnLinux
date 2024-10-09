
package jdk.nashorn.api.test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public final class NashornSQLDriver implements Driver {

    static {
        try {
            DriverManager.registerDriver(new NashornSQLDriver(), null);
        } catch (final SQLException se) {
            throw new RuntimeException(se);
        }
    }

    @Override
    public boolean acceptsURL(final String url) {
        return url.startsWith("jdbc:nashorn:");
    }

    @Override
    public Connection connect(final String url, final Properties info) {
        throw new UnsupportedOperationException("I am a dummy!!");
    }

    @Override
    public int getMajorVersion() {
        return -1;
    }

    @Override
    public int getMinorVersion() {
        return -1;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) {
        return new DriverPropertyInfo[0];
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
