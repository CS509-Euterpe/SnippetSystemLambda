/*
 * CS-509 Team Eutrepe AWS Application
 */

package edu.wpi.cs.eutrepe.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;

import edu.wpi.cs.eutrepe.dto.Language;
import edu.wpi.cs.eutrepe.dto.SnippetDto;

public class SnippetDao {
	java.sql.Connection conn;
	
	final String tblName = "Snippets";   // Exact capitalization
    public SnippetDao() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public SnippetDto getSnippet(Integer id) throws Exception {
    	try {
    		SnippetDto snippet = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName + " WHERE id=?;");
            ps.setInt(1,  id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                snippet = generateSnippet(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return snippet;
    	} catch (Exception e){
        	e.printStackTrace();
            throw new Exception("Failed in getting snippet: " + e.getMessage());
    	}
    }
    
    public ArrayList<SnippetDto> getAllSnippets() throws Exception {
    	try {
    		ArrayList<SnippetDto> snippets = new ArrayList<SnippetDto>();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName + ";");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                snippets.add(generateSnippet(resultSet));
            }
            resultSet.close();
            ps.close();
            
            return snippets;
    	} catch (Exception e){
        	e.printStackTrace();
            throw new Exception("Failed to get all snippets: " + e.getMessage());
    	}
    }
    
    public Integer addSnippet(SnippetDto snippet) throws Exception {
    	try {
    		Integer id = null;
    		PreparedStatement ps = conn.prepareStatement("INSERT INTO " + tblName + " (info,language,timestamp,content,password,name) values(?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,  snippet.getInfo());
            ps.setInt(2,  snippet.getLanguage().ordinal());
            ps.setString(3,  snippet.getTimestamp());
            ps.setString(4,  snippet.getContent());
            ps.setString(5,  snippet.getPassword());
            ps.setString(6,  snippet.getName());
            ps.execute();
            ResultSet resultSet = ps.getGeneratedKeys();
            while(resultSet.next()) {
            	id = resultSet.getInt(1);
            }
    		return id;
    	} catch(Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in adding snippet: " + e.getMessage());
    	}
    }
    
    public Integer modifySnippet(SnippetDto snippet, Integer id) throws Exception {
    	try {
    		PreparedStatement ps = conn.prepareStatement("UPDATE " + tblName + " SET info = ?,language = ? ,timestamp = ?,content = ?,password = ?,name = ? "+"WHERE id = "+id+";", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,  snippet.getInfo());
            ps.setInt(2,  snippet.getLanguage().ordinal());
            ps.setDate(3,  Date.valueOf(snippet.getTimestamp()));
            ps.setString(4,  snippet.getContent());
            ps.setString(5,  snippet.getPassword());
            ps.setString(6,  snippet.getName());
            ps.execute();
            ResultSet resultSet = ps.getGeneratedKeys();
            while(resultSet.next()) {
            	id = resultSet.getInt(1);
            }
    		return id;
    	} catch(Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in modifying snippet: " + e.getMessage());
    	}
    }
    
    public boolean deleteSnippet(Integer id) throws Exception {
    	try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + tblName + " WHERE id = ?;");
            ps.setInt(1, id);
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);
    	} catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in deleting snippet: " + e.getMessage());
    	}
    }

	private SnippetDto generateSnippet(ResultSet resultSet) throws Exception {
		Integer id  = resultSet.getInt("id");
        String content = resultSet.getString("content");
        String info = resultSet.getString("info");
        String timestamp = resultSet.getString("timestamp").toString();
        String name = resultSet.getString("name").toString();
        String password = resultSet.getString("password").toString();
        SnippetDto snippet = new SnippetDto();
        snippet.setContent(content);
        snippet.setId(id);
        snippet.setInfo(info);
        snippet.setLanguage(Language.values()[resultSet.getInt("language")]);
        snippet.setTimestamp(timestamp);
        snippet.setName(name);
        snippet.setPassword(password);
        return snippet;
	}
}
