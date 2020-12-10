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

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.eutrepe.db.CommentDao;
import edu.wpi.cs.eutrepe.dto.CommentDto;
import edu.wpi.cs.eutrepe.http.CommentResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class HandleCreateCommentTest extends LambdaTest{


    @Test
    public void testHandleCreateComment() throws Exception {
        HandleCreateComment handler = new HandleCreateComment();
        //create sample comment 
        CommentDto comment = new CommentDto();
        comment.setSnippetId("90998");
        comment.setText("testcommentText");
        comment.setTimestamp("2020-04-20"); //TODO make sure comment timing is in minutes and seconds
        comment.setName("Tester");
        comment.getRegion().setStartLine(1);
        comment.getRegion().setEndLine(2);
        comment.getRegion().setStartChar(3);
        comment.getRegion().setEndChar(4);
        
        //send comment as if coming from API Gateway 
        System.out.println(comment);
        String input = new Gson().toJson(comment);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        CommentResponse commentResponse = new Gson().fromJson(output.toString(), CommentResponse.class);
        CommentDto savedComment = commentResponse.getComment();
        CommentDao commentDao = new CommentDao();
        
        
        //check that comment response is success
        assertEquals(commentResponse.getHttpCode().intValue(), 200);
        
        //check that comment was added  
        assertNotNull(savedComment.getId());
        comment.setId(savedComment.getId());
        //check that the comments are the same
        assertTrue(savedComment.equals(comment));
        
        
        //check that comment exists
        assertNotNull(commentDao.getComments(Integer.parseInt(comment.getSnippetId())));
        commentDao.deleteComment(savedComment.getId());
        //check that comment was deleted
        ArrayList<CommentDto> empty =  new ArrayList<CommentDto>();
        assertTrue(empty.equals(commentDao.getComments(Integer.parseInt(comment.getSnippetId()))));
    }
    
    @Test
    public void testBadHandleCreateSnippet() throws Exception {
    	
//      String badInput = "{\"foo\": \"bar\"}";
//      InputStream inputStream = new ByteArrayInputStream(badInput.getBytes());
//      OutputStream output = new ByteArrayOutputStream();
//      HandleCreateComment handler = new HandleCreateComment();
//      handler.handleRequest(inputStream, output, createContext("create"));
//      CommentResponse commentResponse = new Gson().fromJson(output.toString(), CommentResponse.class);
//
//      //check that commentResponse if for failure
//      assertTrue(commentResponse.getHttpCode().equals(500));
//      
    }
//    @Test
//    public void testBadHandleCreateSnippet2() throws Exception {
//    	
//      String badInput = "{\"foo\": \"bar\"}";
//      InputStream inputStream = new ByteArrayInputStream(badInput.getBytes());
//      OutputStream output = new ByteArrayOutputStream();
//      HandleCreateComment handler = new HandleCreateComment();
//      handler.handleRequest(inputStream, output, createContext("create"));
//      CommentResponse commentResponse = new Gson().fromJson(output.toString(), CommentResponse.class);
//
//      //check that commentResponse if for failure
//      assertTrue(commentResponse.getHttpCode().equals(500));
//      
//    }
//  public void testBadHandleCreateSnippet() throws IOException {
//  String badInput = "{\"foo\": \"bar\"}";
//  InputStream inputStream = new ByteArrayInputStream(badInput.getBytes());
//  OutputStream output = new ByteArrayOutputStream();
//  HandleCreateSnippet handler = new HandleCreateSnippet();
    
//  SnippetDto snippet = new SnippetDto();
//  assertNull(snippet.getId());
//  handler.handleRequest(inputStream, output, createContext("create"));
//  SnippetResponse snippetResponse = new Gson().fromJson(output.toString(), SnippetResponse.class);
//  assertTrue(snippetResponse.getHttpCode().equals(500));
//}
}
