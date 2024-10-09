
package com.oti.j9.exclude;


public class Exclude {

	private String _fID;
	private String _fReason;
	private long _fExpiry;

	public String getID()     {return _fID;}
	public String getReason() {return _fReason;}
	public long getExpiry() {return _fExpiry;}

	public Exclude(String id, String reason)
	{
		_fID = id;
		_fReason = reason;
	}

	public Exclude(String id, String reason, long expiry)
	{
		this(id, reason);
		_fExpiry = expiry;
	}

}
