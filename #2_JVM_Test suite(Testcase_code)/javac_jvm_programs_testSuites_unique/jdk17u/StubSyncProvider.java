
package util;

import javax.sql.RowSetReader;
import javax.sql.RowSetWriter;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;

public class StubSyncProvider extends SyncProvider {

    private String providerID = "util.StubSyncProvider";

    private String vendorName = "Oracle Corporation";

    private String versionNumber = "1.0";

    @Override
    public String getProviderID() {
        return providerID;
    }

    @Override
    public RowSetReader getRowSetReader() {
        return null;
    }

    @Override
    public RowSetWriter getRowSetWriter() {
        return null;
    }

    @Override
    public int getProviderGrade() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDataSourceLock(int datasource_lock) throws SyncProviderException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getDataSourceLock() throws SyncProviderException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int supportsUpdatableView() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getVersion() {
        return versionNumber;
    }

    @Override
    public String getVendor() {
        return vendorName;
    }
}
