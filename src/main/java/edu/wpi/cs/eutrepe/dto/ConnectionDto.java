package edu.wpi.cs.eutrepe.dto;

public class ConnectionDto {
	String _connectionId;
	
	int _snippetId;
	
	int _key;
	
	public ConnectionDto(int key, String connectionId, int snippetId)
	{
		this._key = key;
		this._connectionId = connectionId;
		this._snippetId = snippetId;
	}
	
	public String getConnectionId()
	{
		return this._connectionId;
	}
	
	public int getSnippetId()
	{
		return this._snippetId;
	}
	
	public int getKey()
	{
		return this._key;
	}
}
