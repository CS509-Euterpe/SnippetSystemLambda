package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.wpi.cs.eutrepe.db.CommentDao;
import edu.wpi.cs.eutrepe.db.SnippetDao;
import edu.wpi.cs.eutrepe.dto.CommentDto;
import edu.wpi.cs.eutrepe.http.CommentResponse;

public class HandleDeleteCommentTest extends LambdaTest{

    @Test
    public void testHandleDeleteComment() throws Exception {
    	HandleDeleteComment handler = new HandleDeleteComment();
        //create sample comment 
        CommentDto comment = new CommentDto();
        comment.setSnippetId("909999");
        comment.setText("testcommentText");
        comment.setTimestamp("2020-04-20"); //TODO make sure comment timing is in minutes and seconds
        comment.setName("Tester");
        comment.getRegion().setStartLine(1);
        comment.getRegion().setEndLine(2);
        comment.getRegion().setStartChar(3);
        comment.getRegion().setEndChar(4);
        
        CommentDao commentDao = new CommentDao();
        int commentId = commentDao.addComment(comment);
        comment.setId(commentId);
        
        //check that comment exists
        assertNotNull(commentDao.getComments(Integer.parseInt(comment.getSnippetId())));
        
       //create JSON comment-ID 
        JsonObject params = new JsonObject();
        JsonObject path = new JsonObject();
        JsonObject id = new JsonObject();
        id.addProperty("comment-id", comment.getId());
        path.add("path", id);
        params.add("params", path);
    
  
        //send comment-ID as if coming from API Gateway 
        String input = new Gson().toJson(params);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        CommentResponse commentResponse = new Gson().fromJson(output.toString(), CommentResponse.class);
        

        //check that delete comment response is success
        assertEquals(commentResponse.getHttpCode().intValue(), 200);
        
        
        //check that comment does not exist in database anymore
        ArrayList<CommentDto> empty =  new ArrayList<CommentDto>();
        assertTrue(empty.equals(commentDao.getComments(Integer.parseInt(comment.getSnippetId()))));
    }
}
