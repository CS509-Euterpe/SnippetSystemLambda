package edu.wpi.cs.eutrepe.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    
    public SnippetDto getSnippet(String id) throws Exception {
    	try {
    		SnippetDto snippet = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName + " WHERE id=?;");
            ps.setString(1,  id);
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
    
    public boolean addSnippet(SnippetDto snippet) throws Exception {
    	try {
    		PreparedStatement ps = conn.prepareStatement("INSERT INTO " + tblName + " (id,info,language,timestamp,content,password,name) values(?,?,?,?,?,?,?);");
            ps.setString(1,  snippet.getId());
            ps.setString(2,  snippet.getInfo());
            ps.setInt(3,  1);
            ps.setDate(4,  snippet.getTimestamp());
            ps.setString(5,  snippet.getContent());
            ps.setString(6,  snippet.getPassword());
            ps.setString(7,  snippet.getName());
            ps.execute();
    		return true;
    	} catch(Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in adding snippet: " + e.getMessage());
    	}
    }

	private SnippetDto generateSnippet(ResultSet resultSet) throws Exception {
        String id  = resultSet.getString("id");
        String content = resultSet.getString("content");
        String info = resultSet.getString("info");
//        Language language = resultSet.getInt("language");
        Date timestamp = resultSet.getDate("timestamp");
        SnippetDto snippet = new SnippetDto();
        snippet.setContent(content);
        snippet.setId(id);
        snippet.setInfo(info);
        snippet.setLanguage(Language.JAVA);
        snippet.setTimestamp(timestamp);
        return snippet;
	}
}
