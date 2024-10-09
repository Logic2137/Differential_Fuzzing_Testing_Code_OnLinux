
package org.openj9.resources.classloader;

import java.io.Serializable;

import javax.sql.RowSetReader;
import javax.sql.RowSetWriter;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;


public class CustomSyncProvider extends SyncProvider implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public String getProviderID() {
		return null;
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
		return 0;
	}

	@Override
	public void setDataSourceLock(int datasource_lock) throws SyncProviderException {
		
	}

	@Override
	public int getDataSourceLock() throws SyncProviderException {
		return 0;
	}

	@Override
	public int supportsUpdatableView() {
		return 0;
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public String getVendor() {
		return null;
	}
}
