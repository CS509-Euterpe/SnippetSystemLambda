package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;

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
        CommentDto comment = new CommentDto();
        comment.setSnippetId("1");
        //comment.setId(id);
        comment.setText("testcommentText");
        comment.setTimestamp(LocalDate.now().toString());
        comment.setName("Tyler");
        comment.getRegion().setStartLine(1);
        comment.getRegion().setEndLine(2);
        comment.getRegion().setStartChar(3);
        comment.getRegion().setEndChar(4);
        
        
        String input = new Gson().toJson(comment);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        CommentResponse commentResponse = new Gson().fromJson(output.toString(), CommentResponse.class);
        CommentDto savedComment = commentResponse.getComment();
        assertNotNull(savedComment.getId());
        comment.setId(savedComment.getId());
        System.out.println(savedComment.toString());
        System.out.println(comment.toString());
        assertTrue(savedComment.equals(comment));
       
        CommentDao commentDao = new CommentDao();
        commentDao.deleteComment(savedComment.getId());
    }
    
    @Test
    public void testBadHandleCreateSnippet() throws Exception {
    	
    	
    }
}
