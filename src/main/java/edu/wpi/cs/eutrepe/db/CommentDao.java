package edu.wpi.cs.eutrepe.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import edu.wpi.cs.eutrepe.dto.CommentDto;
import edu.wpi.cs.eutrepe.dto.Language;
import edu.wpi.cs.eutrepe.dto.SnippetDto;

public class CommentDao {
	java.sql.Connection conn;
	final String tblName = "Comment";   // Exact capitalization

    public CommentDao() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
	
    public ArrayList<CommentDto> getComments(Integer snippetid) throws Exception {
    	try {
    		ArrayList<CommentDto> comments = new ArrayList<CommentDto>();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName + " WHERE snippetId=?;");
            ps.setInt(1, snippetid);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
            	comments.add(generateComment(resultSet));
            }
            resultSet.close();
            ps.close();
            
            return comments;
    	} catch (Exception e){
        	e.printStackTrace();
            throw new Exception("Failed in getting comment: " + e.getMessage());
    	}
    	
    }
    
    public Integer addComment(CommentDto comment) throws Exception {
    	try {
    		Integer id = null;
    		PreparedStatement ps = conn.prepareStatement("INSERT INTO " + tblName + " (snippetId,text,timestamp,start,end) values(?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,  comment.getSnippetID());
            ps.setString(2,  comment.getText());
            ps.setDate(3,  Date.valueOf(comment.getTimestamp())); 
            ps.setInt(4,  comment.getStart());
            ps.setInt(5,  comment.getEnd());
            ps.execute();
            ResultSet resultSet = ps.getGeneratedKeys();
            while(resultSet.next()) {
            	id = resultSet.getInt(1);
            }
    		return id;
    	} catch(Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in adding comment: " + e.getMessage());
    	}
    	
    }
    
    public boolean deleteComment(Integer id) throws Exception{
    	try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + tblName + " WHERE id = ?;");
            ps.setInt(1, id);
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);
    	} catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in deleting comment: " + e.getMessage());
    	}
    	
    }
    
    private CommentDto generateComment(ResultSet resultSet) throws Exception {
		Integer id  = resultSet.getInt("id");
		String snippetID = resultSet.getString("snippetId");
		String text = resultSet.getString("text");
        Integer start = resultSet.getInt("start");
        Integer end = resultSet.getInt("end");
        LocalDate timestamp = resultSet.getDate("timestamp").toLocalDate();
        CommentDto comment = new CommentDto();
        comment.setId(id);
        comment.setSnippetID(snippetID);
        comment.setText(text);
        comment.setStart(start);
        comment.setEnd(end);
        comment.setTimestamp(timestamp);
        return comment;
	}
    
    
}
