/*
 * CS-509 Team Eutrepe AWS Application
 */

package edu.wpi.cs.eutrepe.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import edu.wpi.cs.eutrepe.dto.ConnectionDto;
public class WebsocketDao {
	java.sql.Connection conn;
	final String tblName = "Websockets";   // Exact capitalization

    public WebsocketDao() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
	
    public ArrayList<ConnectionDto> getConnections(Integer snippetid) throws Exception {
    	try {
    		ArrayList<ConnectionDto> connections = new ArrayList<ConnectionDto>();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName + " WHERE snippetId=?;");
            ps.setInt(1, snippetid);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
            	connections.add(generateConnection(resultSet));
            }
            resultSet.close();
            ps.close();
            
            return connections;
    	} catch (Exception e){
        	e.printStackTrace();
            throw new Exception("Failed in getting connections: " + e.getMessage());
    	}
    	
    }
    
    public Integer addConnection(ConnectionDto connection) throws Exception {
    	try {
    		Integer id = null;
    		PreparedStatement ps = conn.prepareStatement("INSERT INTO " + tblName + " (connectionId,snippetId) values(?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,  connection.getConnectionId());
            ps.setInt(2,  connection.getSnippetId());
            ps.execute();
            ResultSet resultSet = ps.getGeneratedKeys();
            while(resultSet.next()) {
            	id = resultSet.getInt(1);
            }
    		return id;
    	} catch(Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in adding connections: " + e.getMessage());
    	}
    	
    }
    
    public boolean deleteConnection(String connectionId) throws Exception{
    	try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + tblName + " WHERE connectionId = ?;");
            ps.setString(1, connectionId);
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected >= 1);
    	} catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in deleting connections: " + e.getMessage());
    	}
    	
    }
    
    private ConnectionDto generateConnection(ResultSet resultSet) throws Exception {
		Integer id  = resultSet.getInt("key");
		Integer snippetId = resultSet.getInt("snippetId");
		String connectionId = resultSet.getString("connectionId");
        
	    return new ConnectionDto(id, connectionId, snippetId);
	}
    
    
}
