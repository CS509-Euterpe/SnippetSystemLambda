package edu.wpi.cs.eutrepe.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    private static final String SAMPLE_INPUT_STRING = "{\"foo\": \"bar\"}";
    private static final String EXPECTED_OUTPUT_STRING = "{\"FOO\": \"BAR\"}";

    @Test
    public void testHandleCreateComment() throws Exception {
        HandleCreateComment handler = new HandleCreateComment();
        CommentDto comment = new CommentDto();
        //comment.setSnippetID(snippetID);
        //comment.setId(id);
        comment.setText("testcommentText");
        comment.setTimestamp(LocalDate.now());
        comment.setStart(1);
        comment.setEnd(2);
        
        
        String input = new Gson().toJson(comment);
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        handler.handleRequest(inputStream, output, createContext("create"));
        CommentResponse commentResponse = new Gson().fromJson(output.toString(), CommentResponse.class);
        CommentDto savedComment = commentResponse.getComment();
        assertNotNull(savedComment.getId());
        comment.setId(savedComment.getId());
        assertEquals(savedComment, comment);
       
        CommentDao commentDao = new CommentDao();
        commentDao.deleteComment(savedComment.getId());
    }
    
    @Test
    public void testBadHandleCreateSnippet() throws Exception {
    	
    	
    }
}
